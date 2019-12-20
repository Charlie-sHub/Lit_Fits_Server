/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import miscellaneous.Decryptor;
import miscellaneous.Encryptor;

/**
 *
 * @author Carlos Mendez
 */
public class Test {
    /*
    public static void main (String[] args){
        try {
            Decryptor decryptor = new Decryptor();
            String emailAddress = decryptor.decypherAES("Nothin personnel kid", "EncodedAddress.dat");
            String password = decryptor.decypherAES("Nothin personnel kid", "EncodedPassword.dat");
            System.out.println(emailAddress);
            System.out.println(password);
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
    public static void main(String[] args) {
        Decryptor decryptor = new Decryptor();
        Encryptor encryptor = new Encryptor();
        try {
            String password = "wew lad";
            byte[] passwordSecret = encryptor.encrypt(password);
            String passwordPlain = decryptor.decypherRSA(passwordSecret.toString());
            System.out.println(passwordPlain);
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
