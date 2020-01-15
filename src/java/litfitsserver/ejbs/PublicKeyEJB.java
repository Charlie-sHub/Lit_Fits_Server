package litfitsserver.ejbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.ejb.Stateless;

/**
 * EJB for sending the public key to the clients
 *
 * @author Carlos Mendez
 */
@Stateless
public class PublicKeyEJB implements LocalPublicKeyEJB {
    @Override
    public byte[] getPublicKey() throws FileNotFoundException, IOException {        
        byte[] publicKey = null;
        String publicKeyPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("publicKey");
        File publicKeyFile = new File(publicKeyPath);
        publicKey = Files.readAllBytes(publicKeyFile.toPath());
        return publicKey;
    }
}
