package service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import litfitsserver.ejbs.LocalCompanyEJB;
import litfitsserver.entities.Company;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

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
        try {
            companyEJB.createCompany(company);
        } catch (CreateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
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
        try {
            companyEJB.editCompany(company);
        } catch (UpdateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * Deletes a Company
     *
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            companyEJB.removeCompany(companyEJB.findCompany(id));
        } catch (ReadException | DeleteException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
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
        Company company = null;
        try {
            company = companyEJB.findCompany(id);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return company;
    }

    /**
     * Gets all the companies
     *
     * @return List
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Company> findAll() {
        List<Company> companies = null;
        try {
            companies = companyEJB.findAllCompanies();
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return companies;
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
        String amount = null;
        try {
            amount = String.valueOf(companyEJB.countCompanies());
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return amount;
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
        Company company = null;
        try {
            company = companyEJB.findCompanyByNif(nif);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return company;
    }
}