package litfitsserver.ejbs;

import java.io.File;
import miscellaneous.EmailService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Date;
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
import miscellaneous.Decryptor;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * EJB for Companies
 *
 * @author Carlos
 */
@Stateless
public class CompanyEJB implements LocalCompanyEJB {
    @PersistenceContext(unitName = "Lit_Fits_ServerPU")
    private EntityManager em;
    @EJB
    LocalGarmentEJB garmentEJB;

    @Override
    public void createCompany(Company company) throws CreateException {
        File keyFile = new File("private.key");
        byte[] privateKey;
        String message = null;
        try {
            //privateKey = new byte[keyFile.available()];
            //keyFile.read(privateKey);
            //decrypt password
            if (companyExists(company.getNif())) {
                throw new Exception("NIF already exists in the database");
            } else {
                String password = company.getPassword();
                company.setPassword(toHash(password));
                company.setLastAccess(new Date());
                company.setLastPasswordChange(new Date());
                em.persist(company);
            }
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
        /*finally {
            if (null != keyFile) {
                try {
                    keyFile.close();
                } catch (IOException ex) {
                    Logger.getLogger(CompanyEJB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }*/
    }

    @Override
    public Company login(Company company) throws NoSuchAlgorithmException, ReadException, NotAuthorizedException {
        Company companyInDB = findCompanyByNif(company.getNif());
        //Decrypt password and set it again for the company
        boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
        if (!rightPassword) {
            throw new NotAuthorizedException("Wrong password");
        }
        companyInDB.setLastAccess(new Date());
        em.merge(companyInDB);
        companyInDB.setGarments(garmentEJB.findGarmentsByCompany(companyInDB.getNif()));
        return companyInDB;
    }

    @Override
    public void editCompany(Company company) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception {
        //Decrypt password
        //This method should receive the original password to make sure the company is the one editing its own data
        Company companyInDB = findCompanyByNif(company.getNif());
        boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
        if (!rightPassword) {
            sendPasswordChangeComfirmationEmail(company);
            //Make a pool for emails
            company.setLastPasswordChange(new Date());
            String password = company.getPassword();
            company.setPassword(toHash(password));
        }
        em.merge(company);
        em.flush();
    }

    @Override
    public void removeCompany(Company company) throws ReadException, DeleteException {
        em.remove(em.merge(company));
    }

    @Override
    public Company findCompany(Long id) throws ReadException {
        return em.find(Company.class, id);
    }

    @Override
    public List<Company> findAllCompanies() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Company.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public int countCompanies() throws ReadException {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Company> rt = cq.from(Company.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public Company findCompanyByNif(String nif) throws ReadException {
        return (Company) em.createNamedQuery("findCompanyByNif").setParameter("nif", nif).getSingleResult();
    }

    @Override
    public void reestablishPassword(String nif) throws ReadException, MessagingException, Exception {
        Company company = findCompanyByNif(nif);
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        company.setPassword(generatedString);
        sendPasswordReestablishmentEmail(company);
        em.merge(company);
    }

    /**
     * Creates a returns the hash value of the given password
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
     * Sends a confirmation email to the address associated with the company
     *
     * @param company
     * @throws MessagingException
     */
    private void sendPasswordChangeComfirmationEmail(Company company) throws MessagingException, Exception {
        Decryptor decryptor = new Decryptor();
        //Fucking paths how do they work? the path should be relative to decryptor i guess
        String emailAddress = decryptor.decypher("Nothin personnel kid", "EncodedAddress.dat");
        String password = decryptor.decypher("Nothin personnel kid", "C:\\Users\\2dam.LAPINF02\\Documents\\NetBeansProjects\\Lit_Fits_Server\\src\\java\\miscellaneous\\EncodedPassword.dat");
        EmailService emailService = new EmailService(emailAddress, password, null, null);
        String text = "The password for the company: " + company.getNif() + " was changed the " + LocalDate.now();
        emailService.sendMail(company.getEmail(), "Your Lit Fits password has been changed", text);
    }

    /**
     * Sends an email notifying the password has been changed to a new random one, used when users forget their
     * passwords
     *
     * @param company
     * @throws MessagingException
     */
    private void sendPasswordReestablishmentEmail(Company company) throws MessagingException, Exception {
        Decryptor decryptor = new Decryptor();
        //Fucking paths how do they work? the path should be relative to decryptor i guess
        String emailAddress = decryptor.decypher("Nothin personnel kid", "miscellaneous/EncodedAddress.dat");
        String password = decryptor.decypher("Nothin personnel kid", "miscellaneous/EncodedPassword.dat");
        EmailService emailService = new EmailService(emailAddress, password, null, null);
        String text = "The password for the company: " + company.getNif() + " was changed the " + LocalDate.now() + ", to " + company.getPassword();
        emailService.sendMail(company.getEmail(), "Your Lit Fits password has been changed", text);
    }

    /**
     * Checks if a company with a given nif exists
     *
     * @param nif
     * @return boolean exists
     */
    private boolean companyExists(String nif) throws ReadException {
        return (long) em.createNamedQuery("companyExists").setParameter("nif", nif).getSingleResult() == 1;
    }
}
