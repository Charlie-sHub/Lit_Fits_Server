package litfitsserver.ejbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        File publicKeyFile = new File("public.key");
        Path publicKeyPath = Paths.get(publicKeyFile.getAbsolutePath());
        publicKey = Files.readAllBytes(publicKeyPath);
        return publicKey;
    }
}
