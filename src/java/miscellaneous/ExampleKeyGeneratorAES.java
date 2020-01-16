package miscellaneous;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <b>Criptograf�a Sim�trica (Clave Secreta)</b> <br/>
 * <br/>
 *
 * En esta clasepermite cifrar un texto mediante una <b>clave secreta</b> y lo
 * guarda en un fichero. La �nica forma de descifrar el texto es mediante dicha
 * clave, que tanto el <u>emisor</u> como el <u>receptor</u> deben conocer.
 *
 * En este caso vamos a utilizar:
 * <ul>
 * <li>El algoritmo AES</li>
 * <li>El modo CBC: Existen dos, el ECB que es sencillo, y el CBC que necesita
 * un vector de inicializaci�n(IV)</li>
 * <li>El padding PKCS5Padding (128): Si el mensaje no es m�ltiplo de la
 * longitud del algoritmo se indica un relleno.</li>
 * </ul>
 * AES solo admite <b>tama�os de clave</b> de 16, 24 o 32 bytes. Se debe
 * proporcionar exactamente ese tama�o de clave o usar una
 * <b>"salt"(Semilla)</b>. En criptograf�a un salt es un dato aleatorio que se
 * usa como una entrada adicional dde cifrazo. En este caso, vamos a utilizar
 * salt para crear una clave de exactamente 16 bytes. <br/>
 * <br/>
 * Generalmente un salt se genera aleatoriamente cuando creas la clave, as� que
 * <u>necesitas guardar</u> la clave y su salt para poder cifrar y descifrar.
 */
public class ExampleKeyGeneratorAES {

    private static byte[] salt = "esta es la salt!".getBytes(); // Exactamente 16 bytes

    /**
     * Cifra un texto con AES, modo CBC y padding PKCS5Padding (sim�trica) y lo
     * retorna
     *
     * @param clave La clave del usuario
     * @param mensaje El mensaje a cifrar
     */
    public String cifrarTexto(String clave, String mensaje) {
        String ret = null;
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        try {

            // Creamos un SecretKey usando la clave + salt
            keySpec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); // AES-128
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

            // Creamos un Cipher con el algoritmos que vamos a usar
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encodedMessage = cipher.doFinal(mensaje.getBytes()); // Mensaje cifrado !!!
            byte[] iv = cipher.getIV(); // vector de inicializaci�n por modo CBC

            // Guardamos el mensaje codificado: IV (16 bytes) + Mensaje
            byte[] combined = concatArrays(iv, encodedMessage);

            fileWriter("c:\\trastero\\EjemploAES.dat", combined);

            ret = new String(encodedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Descifra un texto con AES, modo CBC y padding PKCS5Padding (sim�trica) y
     * lo retorna
     *
     * @param clave La clave del usuario
     */
    private String descifrarTexto(String clave) {
        String ret = null;

        // Fichero le�do
        byte[] fileContent = fileReader("c:\\trastero\\EjemploAES.dat");
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        try {
            // Creamos un SecretKey usando la clave + salt
            keySpec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); // AES-128
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

            // Creamos un Cipher con el algoritmos que vamos a usar
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParam = new IvParameterSpec(Arrays.copyOfRange(fileContent, 0, 16)); // La IV est� aqu�
            cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);
            byte[] decodedMessage = cipher.doFinal(Arrays.copyOfRange(fileContent, 16, fileContent.length));
            ret = new String(decodedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Retorna una concatenaci�n de ambos arrays
     *
     * @param array1
     * @param array2
     * @return Concatenaci�n de ambos arrays
     */
    private byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] ret = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, ret, 0, array1.length);
        System.arraycopy(array2, 0, ret, array1.length, array2.length);
        return ret;
    }

    /**
     * Escribe un fichero
     *
     * @param path Path del fichero
     * @param text Texto a escibir
     */
    private void fileWriter(String path, byte[] text) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna el contenido de un fichero
     *
     * @param path Path del fichero
     * @return El texto del fichero
     */
    private byte[] fileReader(String path) {
        byte ret[] = null;
        File file = new File(path);
        try {
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Main de la clase
     *
     * @param args
     */
    public static void main(String[] args) {
        ExampleKeyGeneratorAES keyGen = new ExampleKeyGeneratorAES();
        String mensajeCifrado = keyGen.cifrarTexto("Clave", "Mensaje super secreto");
        System.out.println("Cifrado! -> " + mensajeCifrado);
        System.out.println("-----------");
        System.out.println("Descifrado! -> " + keyGen.descifrarTexto("Clave"));
        System.out.println("-----------");
    }
}
