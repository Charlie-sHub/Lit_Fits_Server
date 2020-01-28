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
 * Generates the public and private keys for asimetric encryption
 */
public class KeyGen {
    /**
     * Genera el fichero con la clave privada
     */
    public void generateKeys() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keypair = generator.generateKeyPair();
            getPublicKey(keypair);
            getPrivateKey(keypair);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void getPublicKey(KeyPair keypair) throws FileNotFoundException, IOException {
        PublicKey publicKey = keypair.getPublic();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fileOutputStream = new FileOutputStream("public.key");
        fileOutputStream.write(x509EncodedKeySpec.getEncoded());
        fileOutputStream.close();
    }

    private void getPrivateKey(KeyPair keypair) throws IOException, FileNotFoundException {
        FileOutputStream fileOutputStream;
        PrivateKey privateKey = keypair.getPrivate();
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        fileOutputStream = new FileOutputStream("private.key");
        fileOutputStream.write(pKCS8EncodedKeySpec.getEncoded());
        fileOutputStream.close();
    }

    public static void main(String[] args) {
        KeyGen keyGenerator = new KeyGen();
        keyGenerator.generateKeys();
    }
}
