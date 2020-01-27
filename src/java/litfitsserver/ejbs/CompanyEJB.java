package litfitsserver.ejbs;

import java.security.InvalidKeyException;
import litfitsserver.miscellaneous.EmailService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.Company;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;
import litfitsserver.miscellaneous.Decryptor;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * EJB for Companies
 *
 * @author Carlos
 */
@Stateless
public class CompanyEJB implements LocalCompanyEJB {
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager entityManager;
    @EJB
    LocalGarmentEJB garmentEJB;

    @Override
    public void createCompany(Company company) throws CreateException {
        try {
            company.setPassword(Decryptor.decypherRSA(company.getPassword()));
            if (companyExists(company.getNif())) {
                throw new Exception("NIF already exists in the database");
            } else {
                company.setPassword(toHash(company.getPassword()));
                company.setLastAccess(new Date());
                company.setLastPasswordChange(new Date());
                entityManager.persist(company);
            }
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
            throw new CreateException(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CreateException(ex.getMessage());
        }
    }

    @Override
    public Company login(Company company) throws ReadException, NotAuthorizedException, Exception {
        Company companyInDB;
        try {
            companyInDB = findCompanyByNif(company.getNif());
            company.setPassword(Decryptor.decypherRSA(company.getPassword()));
            boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
            if (!rightPassword) {
                throw new NotAuthorizedException("Wrong password");
            }
            companyInDB.setLastAccess(new Date());
            entityManager.merge(companyInDB);
            entityManager.flush();
            companyInDB.setGarments(garmentEJB.findGarmentsByCompany(companyInDB.getNif()));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return companyInDB;
    }

    @Override
    public void editCompany(Company company) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception {
        // This method should receive the original password to make sure the company is the one editing its own data
        Decryptor decryptor = new Decryptor();
        company.setPassword(Decryptor.decypherRSA(company.getPassword()));
        Company companyInDB = findCompanyByNif(company.getNif());
        boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
        if (!rightPassword) {
            EmailService emailService = newEmailService(decryptor);
            emailService.sendCompanyPasswordChangeComfirmationEmail(company);
            // Make a pool for emails if possible
            company.setLastPasswordChange(new Date());
            String password = company.getPassword();
            company.setPassword(toHash(password));
        }
        System.out.println("company in db id: " + companyInDB.getId());
        System.out.println("company from client" + company.getId());
        entityManager.merge(company);
        entityManager.flush();
    }

    @Override
    public void removeCompany(Company company) throws ReadException, DeleteException {
        entityManager.remove(entityManager.merge(company));
    }

    @Override
    public Company findCompany(Long id) throws ReadException {
        return entityManager.find(Company.class, id);
    }

    @Override
    public List<Company> findAllCompanies() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Company.class));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public int countCompanies() throws ReadException {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        Root<Company> rt = cq.from(Company.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        Query q = entityManager.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public Company findCompanyByNif(String nif) throws ReadException {
        Company company = null;
        try {
            company = (Company) entityManager.createNamedQuery("findCompanyByNif").setParameter("nif", nif).getSingleResult();
        } catch (Exception e) {
            throw new ReadException("Username not found");
        }
        return company;
    }

    @Override
    public void reestablishPassword(String nif) throws ReadException, MessagingException, Exception {
        Company company = findCompanyByNif(nif);
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        company.setPassword(generatedString);
        Decryptor decryptor = new Decryptor();
        EmailService emailService = newEmailService(decryptor);
        emailService.sendCompanyPasswordReestablishmentEmail(company);
        entityManager.merge(company);
        entityManager.flush();
    }

    /**
     * Creates a new email service object with the address and password from their respective files
     *
     * @param decryptor
     * @return
     * @throws Exception
     */
    private EmailService newEmailService(Decryptor decryptor) throws Exception {
        String encodedPasswordPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/miscellaneous/EncodedPassword.dat";
        String encodedAddressPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/miscellaneous/EncodedAddress.dat";
        String emailAddress = decryptor.decypherAES("Nothin personnel kid", encodedAddressPath);
        String emailAddressPassword = decryptor.decypherAES("Nothin personnel kid", encodedPasswordPath);
        EmailService emailService = new EmailService(emailAddress, emailAddressPassword, null, null);
        return emailService;
    }

    /**
     * Creates and returns the hash value of the given password
     *
     * @param password
     * @return String hash of the password
     * @throws NoSuchAlgorithmException
     */
    private String toHash(String password) throws NoSuchAlgorithmException {
        String passwordHash;
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA");
        byte dataBytes[] = password.getBytes();
        messageDigest.update(dataBytes);
        byte hash[] = messageDigest.digest();
        passwordHash = new String(hash);
        return passwordHash;
    }

    /**
     * Checks if a company with a given nif exists
     *
     * @param nif
     * @return boolean exists
     */
    private boolean companyExists(String nif) throws ReadException {
        return (long) entityManager.createNamedQuery("companyExists").setParameter("nif", nif).getSingleResult() == 1;
    }
}
