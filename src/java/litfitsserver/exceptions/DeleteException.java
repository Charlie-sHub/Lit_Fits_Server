package litfitsserver.exceptions;

/**
 * Thrown when an error occurs while deleting
 *
 * @author Carlos Mendez
 */
public class DeleteException extends Exception {
    /**
     * Creates a new instance of <code>DeleteException</code> without detail
     * message.
     */
    public DeleteException() {
    }

    /**
     * Constructs an instance of <code>DeleteException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DeleteException(String msg) {
        super(msg);
    }
}
