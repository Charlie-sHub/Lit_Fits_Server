/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.mail.MessagingException;
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
import litfitsserver.entities.FashionExpert;
import litfitsserver.ejbs.LocalExpertEJB;
import litfitsserver.entities.Color;
import litfitsserver.entities.Material;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 *
 * @author Ander
 */

@Path("litfitsserver.entities.fashionexpert")
public class FashionExpertFacadeREST  {

    @EJB
    private LocalExpertEJB expertEJB;
    private static final Logger LOG = Logger.getLogger(FashionExpertFacadeREST.class.getName());

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createExpert(FashionExpert expert) {
        LOG.info("Creating a new Expert");
        try {
            expertEJB.createExpert(expert);
        } catch (CreateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
            
        }
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(FashionExpert expert) {
        LOG.info("Editing an expert");
        try{
            expertEJB.modifyExpert(expert);
        }catch(UpdateException | NoSuchAlgorithmException | MessagingException | ReadException ex){
             LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @DELETE
    @Path("{username}")
    public void remove(@PathParam("username") String username) {
        LOG.info("Deleting an expert");
        try {
            expertEJB.deleteExpert(expertEJB.findExpertByUsername(username));
        } catch (ReadException | DeleteException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        } 
    }
    
    @POST
    @Path("login/")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public FashionExpert login(FashionExpert expert) {
        LOG.info("Login of an expert");
        try {
            expert = expertEJB.login(expert);
        } catch (ReadException | NoSuchAlgorithmException | NotAuthorizedException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return expert;
    }
    
    
    
    
    @GET
    @Path("expert/{username}")
    @Produces({MediaType.APPLICATION_XML})
    public FashionExpert findExpertByUsername(@PathParam("username") String username) {
        LOG.info("Finding an expert");
        FashionExpert expert = null;
        try {
            expert = expertEJB.findExpertByUsername(username);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return expert;
    }
    /*
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public FashionExpert find(@PathParam("id") Long id) {
        LOG.info("Finding an expert");
        FashionExpert expert = null;
        try {
            expert = expertEJB.findExpert(id);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return expert;
    }
    */
    @GET
    @Path("colors/")
    @Produces({MediaType.APPLICATION_XML})
    public List<Color> recommendedColors() {
        LOG.info("Recommended Colors");
        List<Color> colors;
        try {
            colors = expertEJB.getRecommendedColors();
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return colors;
    }
    
    @GET
    @Path("materials/")
    @Produces({MediaType.APPLICATION_XML})
    public List<Material> recommendedMaterials() {
        LOG.info("Recommended Colors");
        List<Material> materials;
        try {
            materials = expertEJB.getRecommendedMaterials();
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return materials;
    }
       

       
   
}
