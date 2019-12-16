/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import javax.ejb.CreateException;
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
import litfitsserver.entities.FashionExpert;
import litfitsserver.ejbs.ExpertManagerLocal;

/**
 *
 * @author Ander
 */

@Path("litfitsserver.entities.fashionexpert")
public class FashionExpertFacadeREST  {

    @EJB
    private ExpertManagerLocal ejb;
    
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createExpert(FashionExpert expert) {
        try {
            ejb.createExpert(expert);
        } catch (CreateException ex) {
            
        }
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(FashionExpert expert) {
        ejb.modifyExpert(expert);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
       ejb.deleteExpert(ejb.findExpert(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public FashionExpert find(@PathParam("id") String id) {
        return ejb.findExpert(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<FashionExpert> findAllExpert(){
        return ejb.findAllExperts();
    }
       
   
}
