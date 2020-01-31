package litfitsserver.ejbs;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.ejb.Local;

/**
 * Interface for the PublicKeyEJB
 *
 * @author Carlos Mendez
 */
@Local
public interface LocalPublicKeyEJB {
    /**
     * Returns the public key in use by the application
     *
     * @return byte[] the public key as an array of bytes
     * @throws FileNotFoundException
     * @throws IOException
     */
    byte[] getPublicKey() throws FileNotFoundException, IOException;
}
