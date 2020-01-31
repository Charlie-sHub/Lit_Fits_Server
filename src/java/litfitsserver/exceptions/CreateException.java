package litfitsserver.exceptions;

/**
 * Exception thrown when an entity failed to be created
 *
 * @author Carlos Mendez
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
