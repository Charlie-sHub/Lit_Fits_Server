/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.ejbs;

import miscellaneous.Decryptor;
import miscellaneous.Decryptor;

/**
 *
 * @author Carlos Mendez
 */
public class Testofencryption {
    public static void main(String[] args) {
        try {
            Decryptor decryptor = new Decryptor();
            String emailAdress = decryptor.decypher("Nothin personnel kid", ".\\EncodedUser.dat");
            String password = decryptor.decypher("Nothin personnel kid", ".\\EncodedPassword.dat");
            System.out.println(emailAdress);
            System.out.println(password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
