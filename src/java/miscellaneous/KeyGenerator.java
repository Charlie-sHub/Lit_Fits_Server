/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Private and Public key generator class
 *
 * @author Carlos Mendez
 */
public class KeyGenerator {

    public static void main(String[] args) {
        generateSaveKeyPair();
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter the email to use: ");
            String user = s.nextLine();
            System.out.println("Enter the password to use: ");
            String password = s.nextLine();
            byte[] salt = user.getBytes();
            KeySpec keySpec = new PBEKeySpec("Whatever".toCharArray(), salt, 65536, 128);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encodedMessage = cipher.doFinal(message.getBytes());
            byte[] iv = cipher.getIV();

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
