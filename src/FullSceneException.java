/**
 * This class represents an exception that is thrown when a SceneNode is full,
 * meaning that there are no available child slots to add a new scene.
 * It extends Exception and carries a custom error message.
 */
public class FullSceneException extends Exception {
    /**
     * Constructs a FullSceneException with the specified message.
     * @param message the error message describing the exception
     */
    public FullSceneException(String message) {
        super(message);
    }
}