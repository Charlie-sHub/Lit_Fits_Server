package miscellaneous;

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
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
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
    private static final byte[] SALT = "OwO UwU *.^ u.u!".getBytes();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the email to use: ");
        String emailAddress = scanner.nextLine();
        System.out.println("Enter the password to use: ");
        String password = scanner.nextLine();
        //Fucking paths how do they work? gotta change them to relative paths
        encrypt("C:\\Users\\2dam.LAPINF02\\Documents\\NetBeansProjects\\Lit_Fits_Server\\src\\java\\miscellaneous\\EncodedAddress.dat", emailAddress);
        encrypt("C:\\Users\\2dam.LAPINF02\\Documents\\NetBeansProjects\\Lit_Fits_Server\\src\\java\\miscellaneous\\EncodedPassword.dat", password);
        generateSaveKeyPair();
    }

    /**
     * Takes a given user (email address) and password, encrypts them and saves them
     *
     * @param path
     * @param secret
     */
    private static void encrypt(String path, String secret) {
        try {
            KeySpec keySpec = new PBEKeySpec("Nothin personnel kid".toCharArray(), SALT, 65536, 128);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encodedSecret = cipher.doFinal(secret.getBytes());
            byte[] iv = cipher.getIV();
            byte[] combinedSecret = concatArrays(iv, encodedSecret);
            fileWriter(path, combinedSecret);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates and saves the pair of keys to be used by the server
     */
    private static void generateSaveKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = generator.genKeyPair();
            X509EncodedKeySpec specPublic = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
            fileWriter("C:\\Users\\2dam.LAPINF02\\Documents\\NetBeansProjects\\Lit_Fits_Server\\src\\java\\litfitsserver\\ejbs\\public.key", specPublic.getEncoded());
            PKCS8EncodedKeySpec specPrivate = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
            fileWriter("C:\\Users\\2dam.LAPINF02\\Documents\\NetBeansProjects\\Lit_Fits_Server\\src\\java\\litfitsserver\\ejbs\\private.key", specPrivate.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Combines two arrays
     *
     * @param array1
     * @param array2
     * @return arrayCombined byte[] combination of the arrays
     */
    private static byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] arrayCombined = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, arrayCombined, 0, array1.length);
        System.arraycopy(array2, 0, arrayCombined, array1.length, array2.length);
        return arrayCombined;
    }

    /**
     * Writes the file
     *
     * @param path
     * @param text
     */
    private static void fileWriter(String path, byte[] text) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(text);
        } catch (IOException ex) {
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
