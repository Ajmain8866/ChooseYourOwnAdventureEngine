import java.util.Scanner;

/**
 * It provides a menu-driven interface for creating, modifying, and navigating the adventure's scene tree.
 */
public class AdventureDesigner {

    /**
     * The main method initializes the Adventure Designer application and presents
     * a menu-driven interface for users to create and navigate the scene tree.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Creating a story...");
        System.out.print("Please enter a title: ");
        String rootTitle = input.nextLine();
        System.out.print("Please enter a scene: ");
        String rootDesc = input.nextLine();

        SceneTree tree = new SceneTree();
        try {
            tree.addNewNode(rootTitle, rootDesc);
        } catch (FullSceneException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Scene #" + tree.getCursor().getSceneID() + " added.");

        boolean done = false;
        while (!done) {
            printMenu();
            System.out.print("\nPlease enter a selection: ");
            String choice = input.nextLine().toUpperCase();
            switch (choice) {
                case "A": {
                    try {
                        System.out.print("\nPlease enter a title: ");
                        String title = input.nextLine();
                        System.out.print("Please enter a scene: ");
                        String sceneDesc = input.nextLine();
                        tree.addNewNode(title, sceneDesc);
                        System.out.println("\nScene #" + tree.getSceneCount() + " added.");
                    } catch (FullSceneException e) {
                        System.out.println("\n" + e.getMessage());
                    }
                    break;
                }
                case "R": {
                    try {
                        System.out.print("Please enter an option (A, B, or C): ");
                        String opt = input.nextLine().trim().toUpperCase();
                        tree.removeScene(opt);
                        System.out.println("First path removed.");
                    } catch (NoSuchNodeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "S": {
                    tree.getCursor().displayFullScene();
                    break;
                }
                case "P": {
                    System.out.println();
                    System.out.println(tree.toString());
                    break;
                }
                case "B": {
                    try {
                        tree.moveCursorBackwards();
                        System.out.println("Successfully moved back to " + tree.getCursor().getTitle() + ".");
                    } catch (NoSuchNodeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "F": {
                    System.out.print("Which option do you wish to go to: ");
                    String opt = input.nextLine().trim().toUpperCase();
                    try {
                        tree.moveCursorForward(opt);
                        System.out.println("Successfully moved to " + tree.getCursor().getTitle() + ".");
                    } catch (NoSuchNodeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "G": {
                    System.out.println("\nNow beginning game...\n");
                    playGame(tree);
                    System.out.println("\nReturning back to creation mode...");
                    break;
                }
                case "N": {
                    String path = tree.getPathFromRoot();
                    System.out.println("\n" + path);
                    break;
                }
                case "M": {
                    System.out.print("Move current scene to: ");
                    int targetID = Integer.parseInt(input.nextLine().trim());
                    try {
                        tree.moveScene(targetID);
                        System.out.println("Successfully moved scene.");
                    } catch (NoSuchNodeException | FullSceneException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case "Q": {
                    System.out.println("Program terminating normally...");
                    done = true;
                    break;
                }
                default: {
                    System.out.println("Invalid menu option.");
                }
            }
        }
    }

    /**
     * Initiates a play-through of the adventure. The game continues until an ending scene is reached,
     * allowing the user to navigate through the tree based on scene options.
     * @param tree the SceneTree representing the adventure
     */
    public static void playGame(SceneTree tree) {
        Scanner input = new Scanner(System.in);
        SceneNode originalCursor = tree.getCursor();
        SceneNode current = tree.getRoot();

        while (true) {
            System.out.println(current.getTitle());
            System.out.println(current.getSceneDescription());
            if (current.isEnding()) {
                System.out.println("\nThe End");
                break;
            }
            System.out.println();
            if (current.getLeft() != null) {
                System.out.println("A) " + current.getLeft().getTitle());
            }
            if (current.getMiddle() != null) {
                System.out.println("B) " + current.getMiddle().getTitle());
            }
            if (current.getRight() != null) {
                System.out.println("C) " + current.getRight().getTitle());
            }
            System.out.print("\nPlease enter an option: ");
            String option = input.nextLine().trim().toUpperCase();
            SceneNode next;
            switch (option) {
                case "A":
                    next = current.getLeft();
                    break;
                case "B":
                    next = current.getMiddle();
                    break;
                case "C":
                    next = current.getRight();
                    break;
                default:
                    System.out.println("Invalid choice, returning to main menu.");
                    tree.setCursor(originalCursor);
                    return;
            }
            if (next == null) {
                System.out.println("That option does not exist.");
                tree.setCursor(originalCursor);
                return;
            }
            current = next;
        }
        tree.setCursor(originalCursor);
    }

    /**
     * Prints the menu options for the Adventure Designer user interface.
     */
    private static void printMenu() {
        System.out.println();
        System.out.println("A) Add Scene");
        System.out.println("R) Remove Scene");
        System.out.println("S) Show Current Scene");
        System.out.println("P) Print Adventure Tree");
        System.out.println("B) Go Back A Scene");
        System.out.println("F) Go Forward A Scene");
        System.out.println("G) Play Game");
        System.out.println("N) Print Path To Cursor");
        System.out.println("M) Move scene");
        System.out.println("Q) Quit");
    }
}
