package litfitsserver.ejbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
        //File file = new File(".\\public.key");
        //publicKey = Files.readAllBytes(file.toPath());
        publicKey = "Whatever dude".getBytes();
        return publicKey;
    }
}
