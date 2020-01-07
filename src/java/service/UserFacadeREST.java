/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
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
import litfitsserver.entities.User;
import litfitsserver.ejbs.LocalUserEJB;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;

/**
 * RESTful class for User entity
 * 
 * @author Asier
 */
@Path("litfitsserver.entities.user")
public class UserFacadeREST {
    
    @EJB
    private LocalUserEJB userEJB;
    private static final Logger LOG = Logger.getLogger(UserFacadeREST.class.getName());

    /**
     * Inserts a new user in the database
     * 
     * @param user The user that will be inserted, with all the data
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createUser(User user) {
        
        try {
            userEJB.createUser(user);
            
        } catch (CreateException createException) {
            LOG.severe(createException.getMessage());
            throw new InternalServerErrorException(createException);
        }
    }

    /**
     *  Edits the data of a registered user
     * 
     * @param id The username of the user that will be modified
     * @param user An User object that contains all the data that will be updated
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void editUser(@PathParam("id") String id, User user) {

        try {
            userEJB.createUser(user);
            
        } catch (CreateException createException) {
            LOG.severe(createException.getMessage());
            throw new InternalServerErrorException(createException);
        }
    }

    /**
     * Removes a user from the database
     * 
     * @param user The user that will be deleted from the database
     */
    @DELETE
    @Path("{id}")
    public void removeUser(@PathParam("id") User user) {

        try {
            userEJB.removeUser(user);
            
        } catch (ReadException | DeleteException removeException) {
            LOG.severe(removeException.getMessage());
            throw new InternalServerErrorException(removeException);
        }
    }

    /**
     * This method gets the data of a registered user
     * 
     * @param id The unique id for the user
     * @return The user with all the data
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User findUser(@PathParam("id") String id) {

        try {
            return userEJB.findUser(id);
            
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
    }

    /**
     * This method returns a list with all the registered users
     * 
     * @return A List with the registered users
     */
    @GET
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
     * Returns the amount of registered users
     * 
     * @return An integer with the number of users
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
}
