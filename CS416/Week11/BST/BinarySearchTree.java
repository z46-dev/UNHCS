import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Evan Parker/CS416
 * @version v0.0.1
 */
public class BinarySearchTree {
    //-------------------- instance variables ---------------------
    private Node   root;
    private int    size;

    //-------------------- constructors --------------------------
    /**
     * Construct an empty tree with no nodes.
     */
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * Helper method for inOrder( Node ).
     *
     * @return String all items from the tree in order
     */
    public String inOrder() {
        return inOrder(root).substring(0, Math.max(0, inOrder(root).length() - 2));
    }

    /**
     * Generate a string containing all items from a subtree by
     * traversing them in order.
     *
     * @param n Node the root of the subtree to be traversed.
     * @return String all items from the tree in order
     */
    public String inOrder(Node n) {
        if (n == null) {
            return "";
        }

        return inOrder(n.left) + n.data + ", " + inOrder(n.right);
    }

    /**
     * The leftmost node from the subtree rooted at n.
     *
     * @param n Node the root of the subtree.
     * @return Node the leftmost node under n.
     */
    public Node leftMost(Node n) {
        if (n == null) {
            return null;
        }

        if (n.left != null) {
            return leftMost(n.left);
        }

        return n;
    }

    /**
     * The ancestor (parent or higher) which hasn't been processed yet.
     *
     * @param n Node the root of the subtree.
     * @return Node the unfinished ancestor of n.
     */
    public Node unfinishedAncestor(Node n) {
        if (n.parent == null) {
            return null;
        }

        if (n.parent.right == n) {
            return unfinishedAncestor(n.parent);
        }

        return n.parent;
    }

    /**
     * An iterator class that traverses the tree in order.
     */
    private class InOrderIterator implements Iterator<Data> {
        private Node next;

        /**
         * Constructor for the iterator.
         */
        InOrderIterator() {
            next = leftMost(root);
        }

        /**
         * Moves to the next node and returns the data from
         * the current node.
         *
         * @return Data the data from the current node.
         */
        public Data next() {
            // if called after all elements traversed
            if (next == null) {
                throw new NoSuchElementException();
            }

            Data ret = next.data;

            if (next.right != null) {
                next = leftMost(next.right);
            } else {
                next = unfinishedAncestor(next);
            }

            return ret;
        }

        /**
         * Indicates whether there is data left to traverse in the tree.
         *
         * @return boolean true if there is data left, false otherwise
         */
        public boolean hasNext() {
            return next != null;
        }
    }


    //////////////////////////////////////////////////////////////////
    // MAKE NO CHANGES BELOW HERE
    //////////////////////////////////////////////////////////////////


    //--------------------- iterator -----------------------------------
    /**
     * Returns a new InOrderIterator from this BST.
     * @return InOrderIterator
     */
    public InOrderIterator iterator() {
        return new InOrderIterator();
    }

    //--------------------- add -----------------------------------
    /**
     * Add a node to the tree in its proper position determined by the
     * "key" field of the Data object. This method uses the addNode
     * recursive utility method.
     *
     * @param data Data         Data object to be added to the tree.
     */
    public void add(Data data) {
        boolean added = true;
        if (root == null) {
            root = new Node(data);
        } else {
            added = addNode(root, data);
        }

        if (added) {
            size++;
        }
    }

    //------------------ addNode( Node, Data ) -----------------------
    /**
     * A recursive method to add a new Data object to the subtree
     * rooted at the first argument.
     *
     * @param parent Node       subtree to which Data is to be added
     * @param newOne Data       Data object to be added to the tree
     * @return boolean          true if add was successful
     */
    private boolean addNode(Node parent, Data newOne) {
        boolean added = true;
        int cmp = newOne.compareTo(parent.data);
        if (cmp < 0) {
            if (parent.left != null) {
                added = addNode(parent.left, newOne);
            } else {
                parent.left = new Node(newOne);
                parent.left.parent = parent;
            }
        } else if (cmp == 0) {
            System.err.println("== key found: Not adding: " + newOne);
            added = false;
        } else {
            if (parent.right != null) {
                added = addNode(parent.right, newOne);
            } else {
                parent.right = new Node(newOne);
                parent.right.parent = parent;
            }
        }
        return added;
    }

    //-------------------- size(  ) -------------------------
    /**
     * Return the number of nodes in the tree.
     * @return int   size of tree
     */
    public int size() {
        return size;
    }

    //-------------------------- toString() -------------------------
    /**
     * Generate a string representation of the tree.
     *
     * @return String       a string representation of the tree.
     */
    public String toString() {
        return toString(root, "  ", "=");
    }

    /**
     * Recursively generate a string representation for a Node of a tree;
     *    indent is increased for increasing depth.
     * Branch is a short string that prefixes each node indicating
     *        whether the node is a left (L) or right (R) child.
     *
     * @param n Node         subtree to convert to string
     * @param indent String  prefix string for indentation level
     * @param branch String  (L) or (R)
     * @return String        string rep of subtree
     */
    private String toString(Node n, String indent, String branch) {
        String s = "";
        if (n != null) {
            String prefix = indent.substring(0, indent.length() - 2)
                                  + branch;
            s += prefix + n.data.toString() + "\n";
            if (n.left != null) {
                s += toString(n.left, indent + "  ", "L ");
            }
            if (n.right != null) {
                s += toString(n.right, indent + "  ", "R ");
            }
        }
        return s;
    }

    //-------------------- findNode( String ) -----------------------
    /**
     * Given a key value, search the tree to find the node that has
     * that key value, if it exists.
     *
     * Return the Data object from the node or null.
     * @param key String
     * @return Node
     */
    public Node findNode(String key) {
        Data found = null;
        Node cur = root;
        while (cur != null && found == null) {
            int cmp = key.compareTo(cur.data.key);
            if (cmp == 0) {
                found = cur.data;
            } else if (cmp < 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return cur;
    }

    //+++++++++++++++++++++++ inner class Node ++++++++++++++++++++++
    /**
     * The Node class does not have to be seen outside this class, so
     * it is private.
     * @author rdb
     */
    public class Node {
        //-------------- instance variables ---------------------------
        Data data;
        Node left;
        Node right;
        Node parent;

        //--------------- constructor --------------------------------
        /**
         * Construct a node with Data.
         *
         * @param  d Data    data for the node.
         */
        public Node(Data d) {
            data = d;
            left = null;
            right = null;
            parent = null;
        }
    }
}