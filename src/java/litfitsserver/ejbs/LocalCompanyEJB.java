package litfitsserver.ejbs;

import java.util.List;
import javax.ejb.Local;
import litfitsserver.entities.Company;

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
    int countCompanies();

    /**
     * Inserts a new company in the database
     *
     * @param company
     */
    void createCompany(Company company);

    /**
     * Edits a Company
     *
     * @param company
     */
    void editCompany(Company company);

    /**
     * Gets all the companies
     *
     * @return List
     */
    List<Company> findAllCompanies();

    /**
     * Gets a Company by its id
     *
     * @param id
     * @return Company
     */
    Company findCompany(Long id);

    /**
     * Deletes a company
     *
     * @param company
     */
    void removeCompany(Company company);
    /**
     * Gets a Company by its nif
     * 
     * @param nif
     * @return Company
     */
    Company findCompanyByNif(String nif);
    
}
