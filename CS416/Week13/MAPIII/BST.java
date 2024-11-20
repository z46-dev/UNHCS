/**
 * @author Evan Parker
 * @version v0.0.1
 * @param <T> Type
 */
public class BST<T extends Comparable<T>> implements Tree<T> {
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
                current = current.left;
                if (current == null) {
                    parent.left = node;
                    node.parent = parent;
                    return true;
                }

                continue;
            }

            if (cmp > 0) {
                current = current.right;
                if (current == null) {
                    parent.right = node;
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
                current = current.left;
                continue;
            }

            if (cmp > 0) {
                current = current.right;
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
        return toString(root);
    }

    /**
     * @param node Node
     * @return String
     */
    private String toString(Node node) {
        if (node == null) {
            return "";
        }

        String out = "";

        if (node.left != null) {
            out += "L " + toString(node.left) + " ";
        }

        out += node.data;

        if (node.right != null) {
            out += " R " + toString(node.right);
        }

        return out;
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

        while (node != null) {
            int cmp = node.data.compareTo(value);

            if (cmp < 0) {
                node = node.left;
                continue;
            }

            if (cmp > 0) {
                node = node.right;
                continue;
            }

            break;
        }

        if (node == null) {
            return false;
        }

        Node parent = node.parent;
        Node replacement = null;
        boolean left = false;

        if (node.left != null && node.right != null) {
            Node temp = node.right;

            while (temp.left != null) {
                temp = temp.left;
            }

            replacement = temp;
        } else if (node.left != null) {
            replacement = node.left;
            left = true;
        } else if (node.right != null) {
            replacement = node.right;
        }

        if (parent == null) {
            root = replacement;
        } else if (left) {
            parent.left = replacement;
        } else {
            parent.right = replacement;
        }

        if (replacement != null) {
            replacement.parent = parent;
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
}
