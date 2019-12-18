package litfitsserver.ejbs;

import miscellaneous.Decryptor;

/**
 *
 * @author Carlos Mendez
 */
public class Testofencryption {
    public static void main(String[] args) {
        try {
            Decryptor decryptor = new Decryptor();
            String emailAdress = decryptor.decypher("Nothin personnel kid", ".\\EncodedUser.dat");
            String password = decryptor.decypher("Nothin personnel kid", ".\\EncodedPassword.dat");
            System.out.println(emailAdress);
            System.out.println(password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
