/**
 * This class represents the scene tree for the Adventure Designer application.
 * It maintains a branching narrative structure, with a root SceneNode, a cursor
 * indicating the current scene, and a scene count to uniquely identify each scene.
 */
public class SceneTree {
    private SceneNode root;
    private SceneNode cursor;
    private int sceneCount;

    /**
     * Constructs an empty SceneTree with no scenes.
     */
    public SceneTree() {
        root = null;
        cursor = null;
        sceneCount = 0;
    }

    /**
     * Gets the root SceneNode of the tree.
     * @return the root SceneNode
     */
    public SceneNode getRoot() {
        return root;
    }

    /**
     * Gets the current cursor SceneNode.
     * @return the cursor SceneNode
     */
    public SceneNode getCursor() {
        return cursor;
    }

    /**
     * Sets the current cursor SceneNode.
     * @param node the SceneNode to set as the cursor
     */
    public void setCursor(SceneNode node) {
        cursor = node;
    }

    /**
     * Gets the total number of scenes in the tree.
     * @return the scene count
     */
    public int getSceneCount() {
        return sceneCount;
    }

    /**
     * Moves the cursor backwards to the parent of the current SceneNode.
     * @throws NoSuchNodeException if the cursor is at the root or no parent is found
     */
    public void moveCursorBackwards() throws NoSuchNodeException {
        if (cursor == root)
            throw new NoSuchNodeException("Already at the root; no parent exists.");
        SceneNode parent = findParent(root, cursor);
        if (parent == null)
            throw new NoSuchNodeException("Parent not found.");
        cursor = parent;
    }

    /**
     * Moves the cursor forwards to one of the child SceneNodes based on the provided option.
     * The option 'A' corresponds to the left child, 'B' to the middle child, and 'C' to the right child.
     * @param option the option representing which child to move to (e.g., "A", "B", "C")
     * @throws NoSuchNodeException if there are no children available or the option is invalid
     */
    public void moveCursorForward(String option) throws NoSuchNodeException {
        option = option.toUpperCase();
        SceneNode[] available = new SceneNode[3];
        int count = 0;
        if (cursor.getLeft() != null)
            available[count++] = cursor.getLeft();
        if (cursor.getMiddle() != null)
            available[count++] = cursor.getMiddle();
        if (cursor.getRight() != null)
            available[count++] = cursor.getRight();
        if (count == 0)
            throw new NoSuchNodeException("No children available.");
        int index = option.charAt(0) - 'A';
        if (index < 0 || index >= count)
            throw new NoSuchNodeException("That option does not exist.");
        cursor = available[index];
    }

    /**
     * Adds a new SceneNode to the tree. If the tree is empty, the new node becomes the root.
     * Otherwise, the new node is added as a child of the current cursor node.
     * @param title the title for the new scene
     * @param sceneDescription the description for the new scene
     * @throws FullSceneException if the current cursor node already has three children
     */
    public void addNewNode(String title, String sceneDescription) throws FullSceneException {
        if (cursor == null) {
            sceneCount++;
            SceneNode newNode = new SceneNode(sceneCount, title, sceneDescription);
            root = newNode;
            cursor = newNode;
        }
        else {
            if (cursor.getLeft() != null && cursor.getMiddle() != null && cursor.getRight() != null)
                throw new FullSceneException("You cannot add another scene!");
            else {
                sceneCount++;
                SceneNode newNode = new SceneNode(sceneCount, title, sceneDescription);
                cursor.addSceneNode(newNode);
            }
        }
    }

    /**
     * Removes a child SceneNode from the current cursor based on the provided option.
     * Option "A" removes the left child, "B" removes the middle child, and "C" removes the right child.
     * Child nodes are shifted accordingly if a removal creates a gap in the left-to-right order.
     * @param option the child option to remove ("A", "B", or "C")
     * @throws NoSuchNodeException if there is no child for the specified option or if the option is invalid
     */
    public void removeScene(String option) throws NoSuchNodeException {
        option = option.toUpperCase();
        if (option.equals("A")) {
            if (cursor.getLeft() == null)
                throw new NoSuchNodeException("No left child to remove.");
            cursor.setLeft(null);
            shiftChildren();
        }
        else if (option.equals("B")) {
            if (cursor.getMiddle() == null)
                throw new NoSuchNodeException("No middle child to remove.");
            cursor.setMiddle(null);
            shiftChildren();
        }
        else if (option.equals("C")) {
            if (cursor.getRight() == null)
                throw new NoSuchNodeException("No right child to remove.");
            cursor.setRight(null);
        }
        else
            throw new NoSuchNodeException("Invalid option: must be 'A', 'B', or 'C'.");
    }

    /**
     * Shifts child nodes of the current cursor to fill in removed child slots.
     * This method is called internally after a removal operation.
     */
    private void shiftChildren() {
        if (cursor.getLeft() == null && cursor.getMiddle() != null) {
            cursor.setLeft(cursor.getMiddle());
            cursor.setMiddle(cursor.getRight());
            cursor.setRight(null);
        }
    }

    /**
     * Moves the current cursor SceneNode to be a child of another SceneNode identified by the given scene ID.
     * The node is removed from its current parent and added as a child to the target SceneNode.
     * @param sceneIDToMoveTo the scene ID of the target parent node
     * @throws NoSuchNodeException if the cursor is at the root or the target node is not found
     * @throws FullSceneException if the target node has no available child slots for adding the cursor
     */
    public void moveScene(int sceneIDToMoveTo) throws NoSuchNodeException, FullSceneException {
        if (cursor == null || cursor == root)
            throw new NoSuchNodeException("Cannot move the root node or a null cursor.");
        SceneNode oldParent = findParent(root, cursor);
        if (oldParent == null)
            throw new NoSuchNodeException("Could not find parent of cursor.");
        SceneNode target = findByID(root, sceneIDToMoveTo);
        if (target == null)
            throw new NoSuchNodeException("No scene with ID " + sceneIDToMoveTo + " found.");
        if (oldParent.getLeft() == cursor)
            oldParent.setLeft(null);
        else if (oldParent.getMiddle() == cursor)
            oldParent.setMiddle(null);
        else if (oldParent.getRight() == cursor)
            oldParent.setRight(null);
        target.addSceneNode(cursor);
    }

    /**
     * Constructs and returns a comma-separated string representing the path from the root node
     * to the current cursor node.
     * @return the formatted path from the root to the current node
     */
    public String getPathFromRoot() {
        return buildPath(root, cursor);
    }

    /**
     * Returns a string representation of the entire SceneTree, including the hierarchical structure
     * of all nodes.
     * @return a formatted string representing the SceneTree structure
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        buildTreeString(root, sb, 0, ' ', true);
        return sb.toString();
    }

    /**
     * Recursively searches for the parent of the given child node starting from the specified current node.
     * @param current the current SceneNode to search from
     * @param child the child SceneNode whose parent is sought
     * @return the parent SceneNode if found; otherwise, null
     */
    private SceneNode findParent(SceneNode current, SceneNode child) {
        if (current == null)
            return null;
        if (current.getLeft() == child || current.getMiddle() == child || current.getRight() == child)
            return current;
        SceneNode found = findParent(current.getLeft(), child);
        if (found != null)
            return found;
        found = findParent(current.getMiddle(), child);
        if (found != null)
            return found;
        found = findParent(current.getRight(), child);
        return found;
    }

    /**
     * Recursively searches for a SceneNode with the given scene ID starting from the specified current node.
     * @param current the current SceneNode to search from
     * @param id the scene ID to find
     * @return the SceneNode with the matching scene ID, or null if no match is found
     */
    private SceneNode findByID(SceneNode current, int id) {
        if (current == null)
            return null;
        if (current.getSceneID() == id)
            return current;
        SceneNode found = findByID(current.getLeft(), id);
        if (found != null)
            return found;
        found = findByID(current.getMiddle(), id);
        if (found != null)
            return found;
        found = findByID(current.getRight(), id);
        return found;
    }

    /**
     * Recursively builds the path from the starting node to the goal node.
     * @param start the starting SceneNode
     * @param goal the target SceneNode
     * @return a formatted string representing the path from start to goal
     */
    private String buildPath(SceneNode start, SceneNode goal) {
        if (start == null)
            return "";
        if (start == goal)
            return start.getTitle();
        String leftPath = buildPath(start.getLeft(), goal);
        if (!leftPath.equals(""))
            return start.getTitle() + ", " + leftPath;
        String midPath = buildPath(start.getMiddle(), goal);
        if (!midPath.equals(""))
            return start.getTitle() + ", " + midPath;
        String rightPath = buildPath(start.getRight(), goal);
        if (!rightPath.equals(""))
            return start.getTitle() + ", " + rightPath;
        return "";
    }

    /**
     * Recursively builds a string representation of the SceneTree for displaying the hierarchical structure.
     * @param node the current SceneNode to process
     * @param sb the StringBuilder used for constructing the tree string
     * @param depth the current depth in the tree
     * @param label a label (e.g., 'A', 'B', 'C') indicating the child's position
     * @param isRoot true if the current node is the root node; false otherwise
     */
    private void buildTreeString(SceneNode node, StringBuilder sb, int depth, char label, boolean isRoot) {
        if (node == null)
            return;
        for (int i = 0; i < depth; i++) {
            sb.append("    ");
        }
        if (isRoot) {
            sb.append(node.toString());
            if (node == cursor)
                sb.append(" *");
            sb.append("\n");
        }
        else {
            sb.append(label).append(") ").append(node.toString());
            if (node == cursor)
                sb.append(" *");
            sb.append("\n");
        }
        buildTreeString(node.getLeft(), sb, depth + 1, 'A', false);
        buildTreeString(node.getMiddle(), sb, depth + 1, 'B', false);
        buildTreeString(node.getRight(), sb, depth + 1, 'C', false);
    }
}
