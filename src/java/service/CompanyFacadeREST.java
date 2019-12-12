package service;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import litfitsserver.ejbs.LocalCompanyEJB;
import litfitsserver.entities.Company;

/**
 * RESTful for Company entity
 *
 * @author Carlos
 */
@Path("litfitsserver.entities.company")
public class CompanyFacadeREST {
    /**
     * Injects the EJB of the entity in question
     */
    @EJB
    private LocalCompanyEJB companyEJB;
    private static final Logger LOG = Logger.getLogger(CompanyFacadeREST.class.getName());

    /**
     * Inserts a new Company
     *
     * @param company
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Company company) {
        companyEJB.createCompany(company);
    }

    /**
     * Edits a Company
     *
     * @param id
     * @param company
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, Company company) {
        companyEJB.editCompany(company);
    }

    /**
     * Deletes a Company
     *
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        companyEJB.removeCompany(companyEJB.findCompany(id));
    }

    /**
     * Gets a Company
     *
     * @param id
     * @return Company
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Company find(@PathParam("id") Long id) {
        return companyEJB.findCompany(id);
    }

    /**
     * Gets all the companies
     *
     * @return List
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Company> findAll() {
        return companyEJB.findAllCompanies();
    }

    /**
     * Gets the amount of companies
     *
     * @return String
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(companyEJB.countCompanies());
    }

    /**
     * Gets a company by its nif
     *
     * @param nif
     * @return Company
     */
    @GET
    @Path("company/{nif}")
    @Produces({MediaType.APPLICATION_XML})
    public Company findCompanyByNif(@PathParam("nif") String nif) {
        return companyEJB.findCompanyByNif(nif);
    }
}
