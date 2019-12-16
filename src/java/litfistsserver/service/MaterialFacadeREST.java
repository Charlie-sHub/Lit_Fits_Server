package litfistsserver.service;

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
import litfitsserver.ejbs.LocalMaterialEJB;
import litfitsserver.entities.Material;
import litfitsserver.exceptions.CreateException;
import litfitsserver.exceptions.DeleteException;
import litfitsserver.exceptions.ReadException;
import litfitsserver.exceptions.UpdateException;

/**
 * RESTful class for the Material entity
 *
 * @author Carlos
 */
@Path("litfitsserver.entities.material")
public class MaterialFacadeREST {
    /**
     * Injects the EJB of the entity in question
     */
    @EJB
    private LocalMaterialEJB materialEJB;
    private static final Logger LOG = Logger.getLogger(MaterialFacadeREST.class.getName());

    /**
     * Inserts a new Material in the database
     *
     * @param material
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Material material) {
        try {
            LOG.info("Creating a new Material");
            materialEJB.createMaterial(material);
        } catch (CreateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * Edits the material
     *
     * @param name
     * @param material
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Material material) {
        try {
            LOG.info("Editing a Material");
            materialEJB.editMaterial(material);
        } catch (UpdateException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * Deletes the material
     *
     * @param name
     */
    @DELETE
    @Path("{name}")
    public void remove(@PathParam("name") String name) {
        try {
            LOG.info("Deleting a Material");
            materialEJB.removeMaterial(materialEJB.findMaterial(name));
        } catch (ReadException | DeleteException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    /**
     * Gets the material wanted
     *
     * @param name
     * @return Material
     */
    @GET
    @Path("{name}")
    @Produces({MediaType.APPLICATION_XML})
    public Material find(@PathParam("name") String name) {
        Material material = null;
        try {
            LOG.info("Finding a Material");
            material = materialEJB.findMaterial(name);
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return material;
    }

    /**
     * Gets all the materials
     *
     * @return List of materials
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Material> findAll() {
        List<Material> materialList = null;
        try {
            LOG.info("Getting all Materials");
            materialList = materialEJB.findAllMaterials();
        } catch (ReadException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return materialList;
    }

    /**
     * Gets the amount of materials
     *
     * @return String amount of materials
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        String amount = null;
        try {
            LOG.info("Counting the Materials");
            amount = String.valueOf(materialEJB.countMaterials());
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return amount;
    }
}
