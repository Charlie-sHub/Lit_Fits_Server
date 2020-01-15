package litfitsserver.service;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import litfitsserver.ejbs.LocalPublicKeyEJB;

/**
 * RESTful class for getting the Public Key
 *
 * @author Carlos
 */
@Path("litfitsserver.encryption.publicKey")
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
     * @return Material
     */
    @GET
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public byte[] getPublicKey() {
        byte[] publicKey = null;
        try {
            LOG.info("Giving the public key to a client");
            publicKey = publicKeyEJB.getPublicKey();
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return publicKey;
    }
}
