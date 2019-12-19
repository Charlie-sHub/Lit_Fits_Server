package miscellaneous;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Has the methods for decyphering the encrypted files used by the server
 *
 * @author Carlos Mendez
 */
public class Decryptor {
    private static final byte[] SALT = "OwO UwU *.^ u.u!".getBytes();

    /**
     * Returns the decyphered content of a given encrypted file
     *
     * @param userKey the user's key
     * @param path the path to the file to decypher
     */
    public String decypher(String userKey, String path) throws Exception {
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
    private byte[] fileReader(String path) {
        byte content[] = null;
        File file = new File(path);
        try {
            Path absolutePath = Paths.get(file.getAbsolutePath());
            content = Files.readAllBytes(absolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
