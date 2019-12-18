package litfitsserver.ejbs;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.ws.rs.NotAuthorizedException;
import litfitsserver.entities.Company;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * Interface for the CompanyEJB
 *
 * @author Carlos
 */
@Local
public interface LocalCompanyEJB {
    /**
     * Gets the amount of companies
     *
     * @return int
     */
    int countCompanies() throws ReadException;

    /**
     * Inserts a new company in the database
     *
     * @param company
     */
    void createCompany(Company company) throws CreateException;

    /**
     * Edits a Company
     *
     * @param company
     */
    void editCompany(Company company) throws UpdateException, NoSuchAlgorithmException, ReadException, MessagingException, Exception;

    /**
     * Gets all the companies
     *
     * @return List
     */
    List<Company> findAllCompanies() throws ReadException;

    /**
     * Gets a Company by its id
     *
     * @param id
     * @return Company
     */
    Company findCompany(Long id) throws ReadException;

    /**
     * Deletes a company
     *
     * @param company
     */
    void removeCompany(Company company) throws ReadException, DeleteException;

    /**
     * Gets a Company by its nif
     *
     * @param nif
     * @return Company
     */
    Company findCompanyByNif(String nif) throws ReadException;

    /**
     * Takes a company object sent by the client and it returns the full company object with that nif and password
     *
     * @param company
     * @return Company
     * @throws NoSuchAlgorithmException
     * @throws ReadException
     * @throws NotAuthorizedException
     */
    public Company login(Company company) throws NoSuchAlgorithmException, ReadException, NotAuthorizedException;

    /**
     * Method to reestablish the password of a given company ( nif)
     *
     * @param nif
     * @throws ReadException
     */
    public void reestablishPassword(String nif) throws ReadException, MessagingException, Exception;
}
