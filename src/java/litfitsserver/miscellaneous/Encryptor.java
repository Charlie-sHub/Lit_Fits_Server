/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.miscellaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 * Class with the methods to encrypt f
 * @author Carlos Mendez
 */
public class Encryptor {
    /**
     * Encrypts a given String
     *
     * @param stringToEncrypt
     * @return byte[] encrypted result
     */
    public byte[] encrypt(String stringToEncrypt) {
        byte[] encodedMessage = null;
        try {
            byte fileKey[] = fileReader("public.key");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileKey);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedMessage = cipher.doFinal(stringToEncrypt.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedMessage;
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
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            content = (byte[]) in.readObject();
            //Path absolutePath = Paths.get(file.getAbsolutePath());
            //content = Files.readAllBytes(absolutePath);
        } catch (IOException ex) {
            //Print stack trace should be removed
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return content;
    }
}
