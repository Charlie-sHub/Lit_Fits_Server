/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Private and Public key generator class
 *
 * @author Carlos Mendez
 */
public class keyGenerator {
    public static void main(String[] args) {
        generateSaveKeyPair();
        try {
            byte[] salt = "lit_fits_no_reply@outlook.com".getBytes();
            KeySpec keySpec = new PBEKeySpec("Whatever".toCharArray(), salt, 65536, 128);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(keyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(keyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void generateSaveKeyPair() {
        FileOutputStream out = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = generator.genKeyPair();
            X509EncodedKeySpec specPublic = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
            out = new FileOutputStream("public.key");
            out.write(specPublic.getEncoded());
            PKCS8EncodedKeySpec specPrivate = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
            out = new FileOutputStream("private.key");
            out.write(specPrivate.getEncoded());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(keyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(keyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(keyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(keyGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
