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
import litfitsserver.ejbs.LocalUserEJB;
import litfitsserver.entities.User;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.ReadException;

/**
 * RESTful for User
 *
 * @author Asier Vila Dominguez
 */
@Path("litfitsserver.entities.user")
public class UserFacadeREST {
    /**
     * Inject EJB 
     */
    @EJB
    private LocalUserEJB userEJB;
    private static final Logger LOG = Logger.getLogger(UserFacadeREST.class.getName());

    /**
     * Inserts a new User
     *
     * @param user
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User user) {
        
        LOG.info("Creating a new user");
        
        try {
            userEJB.createUser(user);
        
        } catch (CreateException createException) {
            LOG.severe(createException.getMessage());
            //createException.printStackTrace();
            throw new InternalServerErrorException(createException);
        
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
            exception.printStackTrace();
            throw new InternalServerErrorException(exception);
        }
    }

    /**
     * Edit a User
     *
     * @param user
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(User user) {
        
        LOG.info("Editing a user");
        
        try {
            userEJB.editUser(user);
        
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new NotFoundException(readException);
        
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
            throw new InternalServerErrorException(exception);
        }
    }

    /**
     * Delete a User
     *
     * @param username
     */
    @DELETE
    @Path("{username}")
    public void remove(@PathParam("username") String username) {
        
        LOG.info("Deleting a company");
        
        try {
            userEJB.removeUser(userEJB.findUser(username));
        
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new NotFoundException(readException);
        
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
            throw new InternalServerErrorException(exception);
        }
    }

    /**
     * Makes a login. Returns the User with all the data.
     * An exception will be thrown if the data does not match.
     *
     * @param user
     * @return User
     */
    @POST
    @Path("login/")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User login(User user) {

        User userLogin = new User();
        LOG.info("User trying to log");
        
        try {
            userLogin = userEJB.login(user);
        
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new NotFoundException(readException);
        
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
            throw new InternalServerErrorException(exception);
        }
        
        return userLogin;
    }

    /**
     * Gets a User using the username of the account.
     *
     * @param username
     * @return User
     */
    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("username") String username) {
        
        LOG.info("Getting user with the username");
        User user = null;
        
        try {
            user = userEJB.findUser(username);
        
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new NotFoundException(readException);
        
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
            throw new InternalServerErrorException(exception);
        }

        return user;
    }

    /**
     * Get all the users from the database.
     *
     * @return List
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() {
        
        LOG.info("Getting all the users");
        List<User> users = null;
        
        try {
            users = userEJB.findAllUsers();
        
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new NotFoundException(readException);
        
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
            throw new InternalServerErrorException(exception);
        }

        return users;
    }

    /**
     * Gets the number of users registered into the database.
     *
     * @return String
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        
        LOG.info("Getting the number of users in the database.");
        String amount = null;

        try {
            amount = String.valueOf(userEJB.countUsers());

        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new NotFoundException(readException);
        }

        return amount;
    }

    /**
     * Replaces the users password with a new random one.
     *
     * @param username
     */
    @GET
    @Path("passwordReestablishment/{username}")
    @Produces({MediaType.TEXT_PLAIN})
    public String reestablishPassword(@PathParam("username") String username) {
        
        LOG.info("Password reestablisment for user.");
        String aux;

        try {
            userEJB.reestablishPassword(username);
            aux = "The Password has been reestablished";

        } catch (ReadException readException) {
            //readException.printStackTrace();
            aux = "There's been an error";
            LOG.severe(readException.getMessage());
            throw new NotFoundException(readException);

        } catch (Exception exception) {
            //exception.printStackTrace();
            aux = "Server internal error.";
            LOG.severe(exception.getMessage());
            throw new InternalServerErrorException(exception);
        }

        return aux;
    }
}