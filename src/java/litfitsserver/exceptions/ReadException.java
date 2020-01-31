package litfitsserver.exceptions;

/**
 * Thrown when an error occurs while reading
 *
 * @author Carlos Mendez
 */
public class ReadException extends Exception {
    /**
     * Creates a new instance of <code>ReadException</code> without detail
     * message.
     */
    public ReadException() {
    }

    /**
     * Constructs an instance of <code>ReadException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ReadException(String msg) {
        super(msg);
    }
}
