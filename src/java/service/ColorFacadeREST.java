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
import litfitsserver.ejbs.LocalColorEJB;
import litfitsserver.entities.Color;
import litfitsserver.exceptions.CreateException;

/**
 * RESTful class for the Color entity
 *
 * @author Carlos
 */
@Path("litfitsserver.entities.color")
public class ColorFacadeREST {
    /**
     * Injects the EJB of the entity in question
     */
    @EJB
    private LocalColorEJB colorEJB;
    private static final Logger LOG = Logger.getLogger(ColorFacadeREST.class.getName());

    /**
     * Inserts a new Color in the database
     *
     * @param color
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Color color) {
        try {
            LOG.info("Creating a new Color");
            colorEJB.createColor(color);
        } catch (CreateException e) {
            LOG.severe(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * Edits the color
     *
     * @param name
     * @param color
     */
    @PUT
    @Path("{name}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("name") String name, Color color) {
        try {
            LOG.info("Editing a Color");
            colorEJB.editColor(color);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Deletes the color
     *
     * @param name
     */
    @DELETE
    @Path("{name}")
    public void remove(@PathParam("name") String name) {
        colorEJB.removeColor(colorEJB.findColor(name));
    }

    /**
     * Gets the color wanted
     *
     * @param name
     * @return Color
     */
    @GET
    @Path("{name}")
    @Produces({MediaType.APPLICATION_XML})
    public Color find(@PathParam("name") String name) {
        return colorEJB.findColor(name);
    }

    /**
     * Gets all the colors
     *
     * @return List of colors
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Color> findAll() {
        return colorEJB.findAllColors();
    }

    /**
     * Gets the amount of colors
     *
     * @return String amount of colors
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(colorEJB.countColors());
    }
}