package service;

import java.util.List;
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
import litfitsserver.ejbs.LocalGarmentEJB;
import litfitsserver.entities.Garment;

/**
 * RESTful for Garment garment
 *
 * @author Carlos
 */
@Path("litfitsserver.entities.garment")
public class GarmentFacadeREST {
    /**
     * Injects the EJB of the entity in question
     */
    @EJB
    private LocalGarmentEJB garmentEJB;

    /**
     * Inserts a new Garment
     *
     * @param garment
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createGarment(Garment garment) {
        garmentEJB.createGarment(garment);
    }

    /**
     * Edits a Garment
     *
     * @param id
     * @param garment
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void editGarment(@PathParam("id") Long id, Garment garment) {
        garmentEJB.editGarment(garment);
    }

    /**
     * Deletes a Garment
     *
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        garmentEJB.removeGarment(garmentEJB.findGarment(id));
    }

    /**
     * Gets an specific Garment
     *
     * @param id
     * @return Garment
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Garment findGarment(@PathParam("id") Long id) {
        return garmentEJB.findGarment(id);
    }

    /**
     * Gets a list of all the garments
     *
     * @return List of Garments
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Garment> findGarmentAll() {
        return garmentEJB.findAllGarments();
    }

    /**
     * Gets the amount of all the garments
     *
     * @return String
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(garmentEJB.countGarments());
    }

    /**
     * Gets the garments of a given Company
     *
     * @param nif
     * @return List
     */
    @GET
    @Path("company/{nif}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Garment> findGarmentGarmentsByCompany(@PathParam("nif") String nif) {
        return garmentEJB.findGarmentsByCompany(nif);
    }

    /**
     * Gets the garments of which a promotion request has been made
     *
     * @param requested
     * @return List
     */
    @GET
    @Path("request/{requested}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Garment> findGarmentGarmentsByRequest(@PathParam("requested") Boolean requested) {
        return garmentEJB.findGarmentsByRequest(requested);
    }

    /**
     * Gets a garment by its barcode
     *
     * @param barcode
     * @return Garment
     */
    @GET
    @Path("barcode/{barcode}")
    @Produces({MediaType.APPLICATION_XML})
    public Garment findGarmentGarmentByBarcode(@PathParam("barcode") String barcode) {
        return garmentEJB.findGarmentByBarcode(barcode);
    }

    /**
     * Gets the garments that are currently being promoted
     *
     * @param promoted
     * @return List
     */
    @GET
    @Path("promotion/{promoted}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Garment> findGarmentGarmentsPromoted(@PathParam("promoted") Boolean promoted) {
        return garmentEJB.findGarmentsPromoted(promoted);
    }
}
