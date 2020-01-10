package litfitsserver.service;

import java.io.File;
import java.io.IOException;
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
import javax.ws.rs.core.Response;
import litfitsserver.ejbs.LocalGarmentEJB;
import litfitsserver.entities.Garment;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
    private static final Logger LOG = Logger.getLogger(GarmentFacadeREST.class.getName());

    /**
     * Inserts a new Garment
     *
     * @param garment
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createGarment(Garment garment) {
        try {
            garmentEJB.createGarment(garment);
        } catch (CreateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * Edits a Garment
     *
     * @param id
     * @param garment
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void editGarment(Garment garment) {
        try {
            garmentEJB.editGarment(garment);
        } catch (UpdateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * Deletes a Garment
     *
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            garmentEJB.removeGarment(garmentEJB.findGarment(id));
        } catch (ReadException | DeleteException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
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
        Garment garment = null;
        try {
            garment = garmentEJB.findGarment(id);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return garment;
    }

    /**
     * Gets a list of all the garments
     *
     * @return List of Garments
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Garment> findGarmentAll() {
        List<Garment> garments = null;
        try {
            garments = garmentEJB.findAllGarments();
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return garments;
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
        String amount = null;
        try {
            amount = String.valueOf(garmentEJB.countGarments());
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return amount;
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
        List<Garment> garments = null;
        try {
            garments = garmentEJB.findGarmentsByCompany(nif);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return garments;
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
        List<Garment> garments = null;
        try {
            garments = garmentEJB.findGarmentsByRequest(requested);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return garments;
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
        Garment garment = null;
        try {
            garment = garmentEJB.findGarmentByBarcode(barcode);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return garment;
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
        List<Garment> garments = null;
        try {
            garments = garmentEJB.findGarmentsPromoted(promoted);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return garments;
    }

    /**
     * Gets the picture of the garment
     *
     * @param id
     * @return Response
     */
    @GET
    @Path("picture/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getImage(@PathParam("id") Long id) {
        File image = null;
        try {
            LOG.info("Retreiving a garment's picture");
            image = garmentEJB.getImage(id);
        } catch (IOException | ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return Response.ok(image, "image/jpg").header("Inline", "filename=\"" + image.getName() + "\"").build();
    }

    /**
     * Takes a given file input and saves it, meant for the garment's picture
     *
     * @param input
     * @return Response
     */
    @PUT
    @Path("picture/upload")
    @Consumes("image/jpg")
    public Response uploadFile(MultipartFormDataInput input, Long id) {
        try {
            if (garmentEJB.uploadPicture(input, id)) {
                return Response.status(200).entity("Uploaded file").build();
            }
        } catch (IOException | ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return null;
    }
}
