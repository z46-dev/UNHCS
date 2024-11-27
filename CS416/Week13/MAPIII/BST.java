/**
 * @author Evan Parker
 * @version v0.0.1
 * @param <T> Type
 */
public class BST<T extends Comparable> implements Tree<T> {
    /**
     * @author Evan Parker
     * @version v0.0.1
     */
    public class Node implements Tree.Node<T> {
        T data;
        Node left;
        Node right;
        Node parent;

        /**
         * Constructor.
         * @param data T
         */
        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;

            // Gradescope yelling at "this.left = this.right = null" makes me sad
        }

        /**
         * @return T
         */
        public T getValue() {
            return data;
        }

        /**
         * @param value T
         */
        public void setValue(T value) {
            data = value;
        }

        /**
         * @return Node
         */
        public Node getLeft() {
            return left;
        }

        /**
         * @return Node
         */
        public Node getRight() {
            return right;
        }

        /**
         * @return Node
         */
        public Node getParent() {
            return parent;
        }
    }
    
    private Node root;

    /**
     * Constructor.
     */
    public BST() {
        root = null;
    }

    /**
     * @return Node
     */
    public Node getRoot() {
        return root;
    }

    /**
     * @param value T
     * @return boolean
     */
    public boolean add(T value) {
        Node node = new Node(value);

        if (root == null) {
            root = node;
            return true;
        }

        Node current = root;
        Node parent = null;

        while (true) {
            parent = current;
            
            int cmp = current.data.compareTo(value);

            if (cmp < 0) {
                current = current.right;
                if (current == null) {
                    parent.right = node;
                    node.parent = parent;
                    return true;
                }

                continue;
            }

            if (cmp > 0) {
                current = current.left;
                if (current == null) {
                    parent.left = node;
                    node.parent = parent;
                    return true;
                }

                continue;
            }

            return false;
        }
    }

    /**
     * @param o Object
     * @return T
     */
    @SuppressWarnings("unchecked")
    public T get(Object o) {
        T value = (T) o;
        Node current = root;

        while (current != null) {
            int cmp = current.data.compareTo(value);

            if (cmp < 0) {
                current = current.right;
                continue;
            }

            if (cmp > 0) {
                current = current.left;
                continue;
            }

            return current.data;
        }

        return null;
    }

    /**
     * @return String
     */
    public String toString() {
        String output = toString(root, 0, 0);

        if (output.endsWith("\n")) {
            output = output.substring(0, output.length() - 1);
        }

        return output;
    }

    /**
     * @param node Node
     * @param indentStage int
     * @param leaning int
     * @return String
     */
    private String toString(Node node, int indentStage, int leaning) {
        /**
         *     L even
         * the
         *         L undo
         *     R word
         *             L zag
         *         R zig
         *             R zoo
         */

        if (node == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append(toString(node.left, indentStage + 1, -1));

        for (int i = 0; i < indentStage; i++) {
            sb.append("      ");
        }

        if (leaning != 0) {
            sb.append(leaning == -1 ? "L " : "R ");
        }

        sb.append(node.data);

        if (node.right != null || indentStage > 0) {
            sb.append("\n");
        }

        sb.append(toString(node.right, indentStage + 1, 1));

        return sb.toString();
    }

    /**
     * Removes every element from the tree.
     */
    public void clear() {
        root = null;
    }

    /**
     * @param o Object
     * @return boolean
     */
    public boolean contains(Object o) {
        return get(o) != null;
    }

    /**
     * @return boolean
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @param o Object
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        Node node = root;
        T value = (T) o;
    
        // Search for the node to be removed
        while (node != null) {
            int cmp = node.data.compareTo(value);
    
            if (cmp < 0) {
                node = node.right;
                continue;
            }
    
            if (cmp > 0) {
                node = node.left;
                continue;
            }
    
            break;
        }
    
        // Node not found
        if (node == null) {
            return false;
        }
    
        // Case 1: Node has no left child
        if (node.left == null) {
            if (node.parent == null) {
                root = node.right;
            } else {
                if (node.parent.data.compareTo(node.data) < 0) {
                    node.parent.right = node.right;
                } else {
                    node.parent.left = node.right;
                }
            }
    
            if (node.right != null) {
                node.right.parent = node.parent;
            }
            return true;
        }
    
        // Case 2: Node has no right child
        if (node.right == null) {
            if (node.parent == null) {
                root = node.left;
            } else {
                if (node.parent.data.compareTo(node.data) < 0) {
                    node.parent.right = node.left;
                } else {
                    node.parent.left = node.left;
                }
            }
    
            if (node.left != null) {
                node.left.parent = node.parent;
            }
            return true;
        }
    
        // Case 3: Node has two children
        Node successor = node.right;
        Node successorParent = node;
    
        while (successor.left != null) {
            successorParent = successor;
            successor = successor.left;
        }
    
        // Replace node's data with successor's data
        node.data = successor.data;
    
        // Remove successor node
        if (successorParent != node) {
            successorParent.left = successor.right;
        } else {
            successorParent.right = successor.right;
        }
    
        if (successor.right != null) {
            successor.right.parent = successorParent;
        }
    
        return true;
    }    

    /**
     * @return int
     */
    public int size() {
        return size(root);
    }

    /**
     * @param node Node
     * @return int
     */
    private int size(Node node) {
        if (node == null) {
            return 0;
        }

        return 1 + size(node.left) + size(node.right);
    }

    /**
     * Debugger main.
     * @param args String[]
     */
    public static void main(String[] args) {
        //    Input: [thing, word, stuff, and, both, zoo, yes]

        BST<String> bst = new BST<>();
        for (String word : new String[] {"thing", "word", "stuff", "and", "both", "zoo", "yes" }) {
            bst.add(word);
        }

        System.out.println(bst);
        System.out.println(bst.size());

        bst.remove("stuff");
        System.out.println(bst);
        System.out.println(bst.size());
    }
}
