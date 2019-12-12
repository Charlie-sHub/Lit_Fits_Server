package service;

import java.util.List;
import java.util.logging.Logger;
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
import litfitsserver.ejbs.LocalMaterialEJB;
import litfitsserver.entities.Material;

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
        materialEJB.createMaterial(material);
    }

    /**
     * Edits the color
     *
     * @param name
     * @param material
     */
    @PUT
    @Path("{name}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("name") String name, Material material) {
        materialEJB.editMaterial(material);
    }

    /**
     * Deletes the material
     *
     * @param name
     */
    @DELETE
    @Path("{name}")
    public void remove(@PathParam("name") String name) {
        materialEJB.removeMaterial(materialEJB.findMaterial(name));
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
        return materialEJB.findMaterial(name);
    }

    /**
     * Gets all the materials
     *
     * @return List of materials
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Material> findAll() {
        return materialEJB.findAllMaterials();
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
        return String.valueOf(materialEJB.countMaterials());
    }
}