package litfitsserver.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import litfitsserver.ejbs.LocalUserEJB;
import litfitsserver.entities.User;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * RESTful class for User entity.
 *
 * @author Asier
 */
@Path("litfitsserver.entities.user")
public class UserFacadeREST {
    @EJB
    private LocalUserEJB userEJB;
    private static final Logger LOG = Logger.getLogger(UserFacadeREST.class.getName());

    /**
     * Inserts a new user in the database.
     *
     * @param user The user that will be inserted, with all the data.
     */
    @POST
    @Path("createuser")
    @Consumes({MediaType.APPLICATION_XML})
    public void createUser(User user) {
        try {
            userEJB.createUser(user);
        } catch (CreateException createException) {
            LOG.severe(createException.getMessage());
            createException.printStackTrace();
            throw new InternalServerErrorException(createException);
        }
    }

    /**
     * Edits the data of a registered user.
     *
     * @param user A User object that contains all the data that will be updated.
     */
    @PUT
    @Path("edit/{username}")
    @Consumes({MediaType.APPLICATION_XML})
    public void editUser(User user) {
        try {
            userEJB.editUser(user);
        } catch (UpdateException updateException) {
            LOG.severe(updateException.getMessage());
            throw new InternalServerErrorException(updateException);
        }
    }

    /**
     * Removes a user from the database.
     *
     * @param username The user that will be deleted from the database.
     */
    @DELETE
    @Path("{username}")
    @Consumes({MediaType.APPLICATION_XML})
    public void removeUser(@PathParam("username") String username) {
        try {
            userEJB.removeUser(username);
        } catch (ReadException | DeleteException removeException) {
            LOG.severe(removeException.getMessage());
            throw new InternalServerErrorException(removeException);
        }
    }

    /**
     * Gets a user with all its data.
     *
     * @param user
     * @return User
     */
    @POST
    @Path("login/")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public User login(User user) {
        LOG.info("User login attempted");
        User returnUser;
        try {
            returnUser = userEJB.login(user);
        } catch (ReadException | NoSuchAlgorithmException | NotAuthorizedException exception) {
            LOG.severe(exception.getMessage());
            throw new InternalServerErrorException(exception);
        } catch (Exception unknownException) {
            unknownException.printStackTrace();
            LOG.severe(unknownException.getMessage());
            throw new InternalServerErrorException(unknownException);
        }
        return returnUser;
    }

    /**
     * This method gets the data of a registered user.
     *
     * @param username The unique id for the user.
     * @return The user with all the data.
     */
    @GET
    @Path("find/{username}")
    @Produces({MediaType.APPLICATION_XML})
    public User findUser(@PathParam("username") String username) {
        try {
            return userEJB.findUser(username);
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
    }

    /**
     * This method returns a list with all the registered users.
     *
     * @return A List with the registered users.
     */
    @GET
    //@Path("findall")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAllUser() {
        try {
            return userEJB.findAllUsers();
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
    }

    /**
     * Returns the amount of registered users.
     *
     * @return An integer with the number of users.
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public int countRESTUser() {
        try {
            return userEJB.countUsers();
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
    }

    /**
     * This method uses an email to filter and get a user from the database.
     *
     * @param email The email that will be used to filter.
     * @return The user with all the data.
     */
    @GET
    @Path("user/{email}")
    @Produces({MediaType.APPLICATION_XML})
    public User findUserByEmail(@PathParam("email") String email) {
        User user = null;
        try {
            user = userEJB.findUserByEmail(email);
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
        return user;
    }

    /**
     * Sets a new password for the user with the received username.
     *
     * @param username The User's username that will receive a new password.
     */
    @GET
    @Path("passwordReestablishment/{username}")
    public void reestablishPassword(@PathParam("username") String username) {
        LOG.info("Reestablishing a user password");
        try {
            userEJB.reestablishPassword(username);
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
}
