package litfitsserver.ejbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
        String publicKeyPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/ejbs/public.key";
        File publicKeyFile = new File(publicKeyPath);
        publicKey = Files.readAllBytes(publicKeyFile.toPath());
        // Don't forget to erase this, it's just a test
        System.out.println(publicKey.length);
        System.out.println(publicKey.toString());
        return publicKey;
    }
}
