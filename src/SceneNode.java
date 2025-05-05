/**
 * This class represents a node in the adventure scene tree. Each SceneNode has a unique
 * scene ID, a title, and a scene description. Additionally, each node may have up to three
 * child nodes (left, middle, and right) that represent branching story options.
 */
public class SceneNode {
    private int sceneID;
    private String title;
    private String sceneDescription;
    private SceneNode left;
    private SceneNode middle;
    private SceneNode right;

    /**
     * Constructs a SceneNode with the given scene ID, title, and scene description.
     * @param id the unique identifier for the scene
     * @param t the title of the scene
     * @param d the description of the scene
     */
    public SceneNode(int id, String t, String d) {
        sceneID = id;
        title = t;
        sceneDescription = d;
    }

    /**
     * Retrieves the scene ID of this node.
     * @return the scene ID
     */
    public int getSceneID() {
        return sceneID;
    }

    /**
     * Retrieves the title of this scene.
     * @return the scene title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this scene.
     * @param t the new title for the scene
     */
    public void setTitle(String t) {
        title = t;
    }

    /**
     * Retrieves the scene description of this node.
     * @return the scene description
     */
    public String getSceneDescription() {
        return sceneDescription;
    }

    /**
     * Sets the scene description of this node.
     * @param s the new scene description
     */
    public void setSceneDescription(String s) {
        sceneDescription = s;
    }

    /**
     * Retrieves the left child SceneNode.
     * @return the left child SceneNode, or null if none exists
     */
    public SceneNode getLeft() {
        return left;
    }

    /**
     * Sets the left child SceneNode.
     * @param l the SceneNode to set as the left child
     */
    public void setLeft(SceneNode l) {
        left = l;
    }

    /**
     * Retrieves the middle child SceneNode.
     * @return the middle child SceneNode, or null if none exists
     */
    public SceneNode getMiddle() {
        return middle;
    }

    /**
     * Sets the middle child SceneNode.
     * @param m the SceneNode to set as the middle child
     */
    public void setMiddle(SceneNode m) {
        middle = m;
    }

    /**
     * Retrieves the right child SceneNode.
     * @return the right child SceneNode, or null if none exists
     */
    public SceneNode getRight() {
        return right;
    }

    /**
     * Sets the right child SceneNode.
     * @param r the SceneNode to set as the right child
     */
    public void setRight(SceneNode r) {
        right = r;
    }

    /**
     * Adds a child SceneNode to this node. The child is added to the first available
     * slot among left, middle, and right.
     * @param child the SceneNode to add as a child
     * @throws FullSceneException if all child slots are already occupied
     */
    public void addSceneNode(SceneNode child) throws FullSceneException {
        if (left == null)
            left = child;
        else if (middle == null)
            middle = child;
        else if (right == null)
            right = child;
        else
            throw new FullSceneException("No available child slots in this SceneNode.");
    }

    /**
     * Determines whether this SceneNode is an ending scene (i.e., it has no children).
     * @return true if no children exist; false otherwise
     */
    public boolean isEnding() {
        return left == null && middle == null && right == null;
    }

    /**
     * Displays the full details of this scene, including its ID, title, description,
     * and the titles and IDs of any child scenes.
     */
    public void displayFullScene() {
        System.out.println("Scene ID #" + sceneID);
        System.out.println("Title: " + title);
        System.out.println("Scene: " + sceneDescription);
        if (left != null || middle != null || right != null) {
            System.out.print("Leads to: ");
            boolean printedOne = false;
            if (left != null) {
                System.out.print("'" + left.title + "' (#" + left.sceneID + ")");
                printedOne = true;
            }
            if (middle != null) {
                if (printedOne)
                    System.out.print(", ");
                System.out.print("'" + middle.title + "' (#" + middle.sceneID + ")");
                printedOne = true;
            }
            if (right != null) {
                if (printedOne)
                    System.out.print(", ");
                System.out.print("'" + right.title + "' (#" + right.sceneID + ")");
            }
            System.out.println();
        } else {
            System.out.println("Leads to: NONE");
        }
    }

    /**
     * Returns a string representation of the SceneNode, including its title and scene ID.
     * @return a formatted string representing the SceneNode
     */
    public String toString() {
        return title + " (#" + sceneID + ")";
    }
}
