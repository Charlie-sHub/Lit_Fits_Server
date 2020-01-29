package litfitsserver.service;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import litfitsserver.ejbs.LocalCompanyEJB;
import litfitsserver.entities.Company;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.ReadException;

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
     * Creates a new Company
     *
     * @param company
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Company company) {
        LOG.info("Creating a company");
        try {
            companyEJB.createCompany(company);
        } catch (CreateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * Edits a Company including the change of password
     *
     * @param id
     * @param company
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Company company) {
        LOG.info("Editing a company");
        try {
            companyEJB.editCompany(company);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
        } catch (Exception ex) {
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
        LOG.info("Deleting a company");
        try {
            companyEJB.removeCompany(companyEJB.findCompany(id));
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * The log in method for companies Takes a Company object with only the password and NIF giving back either an
     * exception or a full Company
     *
     * @param company
     * @return Company
     */
    @POST
    @Path("login/")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public Company login(Company company) {
        LOG.info("Login of a company attempted");
        try {
            company = companyEJB.login(company);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return company;
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
        LOG.info("Finding a company");
        Company company = null;
        try {
            company = companyEJB.findCompany(id);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
        } catch (Exception ex) {
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
        LOG.info("Finding all companies");
        List<Company> companies = null;
        try {
            companies = companyEJB.findAllCompanies();
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
        } catch (Exception ex) {
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
        LOG.info("Counting the companies");
        String amount = null;
        try {
            amount = String.valueOf(companyEJB.countCompanies());
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
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
        LOG.info("Finding a company by its nif");
        Company company = null;
        try {
            company = companyEJB.findCompanyByNif(nif);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return company;
    }

    /**
     * Gets a given nif and replaces the password of the associated company with a generated random one
     *
     * @param nif
     */
    @GET
    @Path("passwordReestablishment/{nif}")
    @Produces({MediaType.TEXT_PLAIN})
    public String reestablishPassword(@PathParam("nif") String nif) {
        LOG.info("Reestablishing a password");
        String aux;
        try {
            companyEJB.reestablishPassword(nif);
            aux = "The Password has been reestablished";
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new NotFoundException(ex);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return aux;
    }
}
