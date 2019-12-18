package litfitsserver.ejbs;

import java.util.ResourceBundle;
import miscellaneous.Decryptor;

/**
 *
 * @author Carlos Mendez
 */
public class Testofencryption {
    public static void main(String[] args) {
        try {
            Decryptor decryptor = new Decryptor();
            String emailAddress = decryptor.decypher("Nothin personnel kid", "EncodedUser.dat");
            String password = decryptor.decypher("Nothin personnel kid", "EncodedPassword.dat");
            System.out.println(emailAddress);
            System.out.println(password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
