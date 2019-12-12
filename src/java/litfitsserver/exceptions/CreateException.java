/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.exceptions;

/**
 * Exception thrown when an entity failed to be created
 *
 * @author Carlos
 */
public class CreateException extends Exception {
    /**
     * Simply instances the exception
     */
    public CreateException() {
    }

    /**
     * Instances the exception with a given message
     *
     * @param message
     */
    public CreateException(String message) {
        super(message);
    }
}
