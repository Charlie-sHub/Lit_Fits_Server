package litfitsserver.ejbs;

import java.io.FileInputStream;
import java.io.IOException;
import miscellaneous.EmailService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        FileInputStream keyFile = null;
        try {
            keyFile = new FileInputStream("private.key");
            byte[] privateKey = new byte[keyFile.available()];
            keyFile.read(privateKey);
            //decrypt password
            if (companyExists(company.getNif())) {
                throw new CreateException("The NIF given already exists in the database");
            } else {
                String password = company.getPassword();
                company.setPassword(toHash(password));
                em.persist(company);
            }
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        } finally {
            if (null != keyFile) {
                try {
                    keyFile.close();
                } catch (IOException ex) {
                    Logger.getLogger(CompanyEJB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public Company login(Company company) throws NoSuchAlgorithmException, ReadException, NotAuthorizedException {
        Company companyInDB = findCompanyByNif(company.getNif());
        //Decrypt password and set it again for the company
        boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
        if (!rightPassword) {
            throw new NotAuthorizedException("Wrong password");
        }
        companyInDB.setGarments(garmentEJB.findGarmentsByCompany(companyInDB.getNif()));
        Date date = new Date();
        companyInDB.setLastAccess(date);
        em.merge(companyInDB);
        return companyInDB;
    }

    @Override
    public void editCompany(Company company) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException {
        //Decrypt password
        //This method should receive the original password to make sure the company is the one editing its own data
        Company companyInDB = findCompanyByNif(company.getNif());
        boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
        if (!rightPassword) {
            sendPasswordComfirmationEmail(company);
            //Make a pool for emails
            company.setPassword(toHash(company.getPassword()));
            Date date = new Date();
            company.setLastPasswordChange(date);
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
    private void sendPasswordComfirmationEmail(Company company) throws MessagingException {
        String user = ResourceBundle.getBundle("litfistsserver.miscellaneous.emailCredentials").getString("user");
        String password = ResourceBundle.getBundle("litfistsserver.miscellaneous.emailCredentials").getString("password");
        //Decypher credentials
        EmailService emailService = new EmailService(user, password, null, null);
        String text = "The password for the company: " + company.getNif() + " was changed the " + LocalDate.now();
        emailService.sendMail(company.getEmail(), "Your Lit Fits password has been changed", text);
    }

    /**
     * Checks if a company with a given nif exists
     *
     * @param nif
     * @return Boolenan exists
     */
    private boolean companyExists(String nif) throws ReadException {
        return (int) em.createNamedQuery("companyExists").setParameter("nif", nif).getSingleResult() == 1;
    }
}
