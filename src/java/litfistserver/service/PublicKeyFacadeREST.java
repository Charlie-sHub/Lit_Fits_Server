package litfistserver.service;

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
import litfitsserver.ejbs.LocalPublicKeyEJB;
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
public class PublicKeyFacadeREST {
    /**
     * Injects the EJB of the entity in question
     */
    @EJB
    private LocalPublicKeyEJB publicKeyEJB;
    private static final Logger LOG = Logger.getLogger(PublicKeyFacadeREST.class.getName());

    /**
     * Gets the material wanted
     *
     * @param name
     * @return Material
     */
    @GET
    @Path("publicKey")
    @Produces({MediaType.APPLICATION_XML})
    public byte[] getPublicKey() {
        LOG.info("Giving the public key to a client");
        byte[] publicKey = null;
        try {
            LOG.info("Finding a Material");
            publicKey = publicKeyEJB.getPublicKey();
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return publicKey;
    }
}
