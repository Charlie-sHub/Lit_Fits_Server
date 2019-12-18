package litfitsserver.ejbs;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.ejb.Local;

/**
 * Interface for the publicKeyEJB
 *
 * @author Carlos Mendez
 */
@Local
public interface LocalPublicKeyEJB {
    byte[] getPublicKey() throws FileNotFoundException, IOException;
}
