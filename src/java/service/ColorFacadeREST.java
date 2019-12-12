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
import litfitsserver.ejbs.LocalColorEJB;
import litfitsserver.entities.Color;

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

    /**
     * Inserts a new Color in the database
     *
     * @param color
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Color color) {
        colorEJB.createColor(color);
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
        colorEJB.editColor(color);
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
