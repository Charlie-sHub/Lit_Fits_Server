/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
//import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
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
//import litfitsserver.exceptions.UpdateException;

/**
 * RESTful class for User entity
 * @author Asier
 */
//@Stateless
@Path("litfitsserver.entities.user")
public class UserFacadeREST {

    //@PersistenceContext(unitName = "Test_Server2PU")
    //private EntityManager em;

    //public UserFacadeREST () {
    //    super(User.class);
    //}
    
    @EJB
    private LocalUserEJB userEJB;
    private static final Logger LOG = Logger.getLogger(UserFacadeREST.class.getName());

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createUser (User user) {
        //super.create(entity);
        try {
            userEJB.createUser(user);
            
        } catch (CreateException createException) {
            LOG.severe(createException.getMessage());
            throw new InternalServerErrorException(createException);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void editUser (@PathParam("id") String id, User user) {
        //super.edit(entity);
        try {
            userEJB.createUser(user);
            
        } catch (CreateException createException) {
            LOG.severe(createException.getMessage());
            throw new InternalServerErrorException(createException);
        }
    }

    @DELETE
    @Path("{id}")
    public void removeUser (@PathParam("id") User user) {
        //super.remove(super.find(id));
        try {
            userEJB.removeUser(user);
            
        } catch (ReadException | DeleteException removeException) {
            LOG.severe(removeException.getMessage());
            throw new InternalServerErrorException(removeException);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User findUser (@PathParam("id") String id) {
        //return super.find(id);
        try {
            return userEJB.findUser(id);
            
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAllUser () {
        //return super.findAll();
        try {
            return userEJB.findAllUsers();
            
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
    }

    //@GET
    //@Path("{from}/{to}")
    //@Produces({MediaType.APPLICATION_XML})
    //public List<User> findRangeUser (@PathParam("from") Integer from, @PathParam("to") Integer to) {
    //    //return super.findRange(new int[]{from, to});
    //    try {
    //        userEJB.findRangeUser();
    //        
    //    } catch (ReadException readException) {
    //        LOG.severe(readException.getMessage());
    //        throw new InternalServerErrorException(readException);
    //    }
    //}

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public int countRESTUser () {
        //return String.valueOf(super.count());
        try {
            return userEJB.countUsers();
            
        } catch (ReadException readException) {
            LOG.severe(readException.getMessage());
            throw new InternalServerErrorException(readException);
        }
    }

    //protected EntityManager getEntityManager() {
    //    return em;
    //}
}
