package litfitsserver.miscellaneous;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Generates the public and private keys for asymmetric encryption, meant to only be used once
 *
 * @author Carlos Mendez
 */
public class KeyGenerator {
    public static void main(String[] args) {
        KeyGenerator keyGenerator = new KeyGenerator();
        keyGenerator.generateKeys();
    }

    /**
     * Generates the pair of keys
     */
    public void generateKeys() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keypair = generator.generateKeyPair();
            getAndStorePublicKey(keypair);
            getAndStorePrivateKey(keypair);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the public key from the keyPair and stores it in a file
     *
     * @param keypair
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void getAndStorePublicKey(KeyPair keypair) throws FileNotFoundException, IOException {
        PublicKey publicKey = keypair.getPublic();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fileOutputStream = new FileOutputStream("public.key");
        fileOutputStream.write(x509EncodedKeySpec.getEncoded());
        fileOutputStream.close();
    }

    /**
     * Gets the private key from the keyPair and stores it in a file
     *
     * @param keypair
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void getAndStorePrivateKey(KeyPair keypair) throws FileNotFoundException, IOException {
        PrivateKey privateKey = keypair.getPrivate();
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        FileOutputStream fileOutputStream = new FileOutputStream("private.key");
        fileOutputStream.write(pKCS8EncodedKeySpec.getEncoded());
        fileOutputStream.close();
    }
}
