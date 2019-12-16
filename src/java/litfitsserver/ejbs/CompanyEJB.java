package litfitsserver.ejbs;

import miscellaneous.EmailService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
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
        try {
            String password = company.getPassword();
            company.setPassword(toHash(password));
            em.persist(company);
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

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

    @Override
    public Company login(Company company) throws NoSuchAlgorithmException, ReadException, NotAuthorizedException {
        Company companyInDB = findCompanyByNif(company.getNif());
        //Decrypt password and set it again for the company
        boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
        if (!rightPassword) {
            throw new NotAuthorizedException("Wrong password");
        }
        companyInDB.setGarments(garmentEJB.findGarmentsByCompany(companyInDB.getNif()));
        return companyInDB;
    }

    @Override
    public void editCompany(Company company) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException {
        //Decrypt password
        Company companyInDB = findCompanyByNif(company.getNif());
        boolean rightPassword = companyInDB.getPassword().equals(toHash(company.getPassword()));
        if (!rightPassword) {
            sendPasswordComfirmationEmail(company);
            String passwordHash = toHash(company.getPassword());
            company.setPassword(passwordHash);
        }
        em.merge(company);
        em.flush();
    }

    private void sendPasswordComfirmationEmail(Company company) throws MessagingException {
        EmailService emailService = new EmailService("lit_fits_no_reply@outlook.com", "litfits69", null, null);
        String text = "The password for the company: " + company.getNif() + " was changed the " + LocalDate.now();
        emailService.sendMail(company.getEmail(), "Your Lit Fits password has been changed", text);
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
}
