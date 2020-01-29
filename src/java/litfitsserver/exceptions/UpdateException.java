package litfitsserver.exceptions;

/**
 * Thrown when an error occurs while updating
 *
 * @author Carlos Mendez
 */
public class UpdateException extends Exception {
    /**
     * Creates a new instance of <code>UpdateException</code> without detail
     * message.
     */
    public UpdateException() {
    }

    /**
     * Constructs an instance of <code>UpdateException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public UpdateException(String msg) {
        super(msg);
    }
}
