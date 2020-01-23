package litfitsserver.ejbs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import org.apache.commons.io.IOUtils;

/**
 * EJB for sending the public key to the clients
 *
 * @author Carlos Mendez
 */
@Stateless
public class PublicKeyEJB implements LocalPublicKeyEJB {
    @Override
    public byte[] getPublicKey() throws FileNotFoundException, IOException {        
        byte[] publicKeyBytes = null;
        // String publicKeyPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/ejbs/public.key";
        File publicKeyFile = new File("public.key");
        FileInputStream input = new FileInputStream(publicKeyFile);
        publicKeyBytes = IOUtils.toByteArray(input);
        return publicKeyBytes;
    }
}
