package litfitsserver.miscellaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.IOUtils;

/**
 * Has the methods for deciphering the encrypted files used by the server
 *
 * @author Carlos Mendez
 */
public class Decryptor {
    private static final byte[] SALT = "OwO UwU *.^ u.u!".getBytes();

    /**
     * Returns the deciphered content of a string
     *
     * @param secret
     * @return String deciphered secret
     * @throws java.security.spec.InvalidKeySpecException
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     * @throws java.io.IOException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     */
    public static String decypherRSA(String secret) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        String message = null;
        byte privateKeyBytes[] = getPrivateKey();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        message = new String(cipher.doFinal(getBytesFromHexString(secret)));
        return message;
    }

    /**
     * Reads the private.key file and returns its contents
     *
     * @return byte[]
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static byte[] getPrivateKey() throws IOException, FileNotFoundException {
        byte privateKeyBytes[] = null;
        String privateKeyPath = ResourceBundle.getBundle("litfitsserver.miscellaneous.paths").getString("serverLocalSystemAddress") + "/ejbs/private.key";
        File privateKeyFile = new File(privateKeyPath);
        FileInputStream input = new FileInputStream(privateKeyFile);
        privateKeyBytes = IOUtils.toByteArray(input);
        return privateKeyBytes;
    }

    /**
     * Gets a byte array from a given string of hexadecimal values
     *
     * @param secret
     * @return byte[]
     */
    public static byte[] getBytesFromHexString(String secret) {
        int length = secret.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(secret.charAt(i), 16) << 4) + Character.digit(secret.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Returns the deciphered content of a given encrypted file
     *
     * @param userKey the user's key
     * @param path the path to the file to decipher
     * @return String deciphered
     * @throws java.lang.Exception
     */
    public String decypherAES(String userKey, String path) throws Exception {
        String decypheredSecret = null;
        byte[] fileContent = fileReader(path);
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        keySpec = new PBEKeySpec(userKey.toCharArray(), SALT, 65536, 128);
        secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
        SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParam = new IvParameterSpec(Arrays.copyOfRange(fileContent, 0, 16));
        cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);
        byte[] decodedMessage = cipher.doFinal(Arrays.copyOfRange(fileContent, 16, fileContent.length));
        decypheredSecret = new String(decodedMessage);
        return decypheredSecret;
    }

    /**
     * Returns the content of a given file
     *
     * @param path
     * @return byte[] the content of the file
     */
    private byte[] fileReader(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        byte[] content = null;
        File file = new File(path);
        FileInputStream input = new FileInputStream(file);
        content = IOUtils.toByteArray(input);
        return content;
    }
}
