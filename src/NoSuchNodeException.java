/**
 * This class represents an exception that is thrown when a specified SceneNode
 * could not be found (e.g., when trying to navigate to or remove a non-existent child node).
 */
public class NoSuchNodeException extends Exception {
    /**
     * Constructs a NoSuchNodeException with the specified message.
     * @param message the error message describing the exception
     */
    public NoSuchNodeException(String message) {
        super(message);
    }
}