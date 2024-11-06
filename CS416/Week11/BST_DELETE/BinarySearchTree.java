/**
 * BinarySearchTree -- this class uses a private inner class for
 * tree nodes. (Although in this version it is public so I can
 * do a prefix walk in DrawPanel.)
 *
 * @author rdb
 * @version April 2, 2008
 *
 *          Modified: April 6
 *          added iterator and node deletion code.
 *
 *          modified mlb Nov 2008 for BSTDeleteApp
 *          Modified rdb Mar 30, 2009
 *          -reorganized delete code to better match notes;
 *          -corrected errors in parent setting -- there needed to be some
 *          parent
 *          changes in addToFarRight and addToFarLeft.
 *          -added some extraneous code to check that all code gets executed.
 *          -figured out 1 data generation and order of deletion that
 *          executes all paths in the remove node code (if the code is correct)
 *          Generate 14 data elements using seed 2 (these are now the
 *          defaults in TreeApp)
 *          Delete nodes in this order:
 *          no, gd, un, wb, hm, aw, re, pv, [sg, fp, gk, kh, ka, ke]
 *          When you hit the bracket, all the rest are root nodes.
 *          05/04/09 rdb: Added delete( String ) method;
 *          revised delete( Data ) method to call new one.
 *          03/31/10 rdb: Made removeNode( Node ) package access so can be
 *          called
 *          from TreeApp for deleting by mouse pick
 *          03/30/11 rdb: Added inOrderString() and inOrderString( Node )
 *          03/23/14 rdb: Added unit testing in main for addToFarRight and
 *          addToFarLeft, along with checkParentLinks and some size
 *          tests.
 *          Solution version only:
 *          Added coverage information to make sure that tests were covering
 *          all cases. This code is not included in start code.
 */
public class BinarySearchTree {
    // -------------------- instance variables ---------------------
    private Node root;
    private int size;

    // -------------------- constructors --------------------------
    /**
     * Construct an empty tree with no nodes.
     */
    public BinarySearchTree() {
        root = null;
    }

    /**
     * Construct a tree with a root .
     * 
     * @param rootData Data
     */
    public BinarySearchTree(Data rootData) {
        this();
        root = new Node(rootData);
    }

    // +++++++++++++++++++++++ inner class Node ++++++++++++++++++++++
    /**
     * The Node class does not have to be seen outside this class, so
     * it is private.
     */
    public static class Node {
        // -------------- instance variables ---------------------------
        Data data;
        Node left;
        Node right;
        Node parent;

        // --------------- constructor --------------------------------
        /**
         * Constructor.
         * 
         * @param d Data
         */
        public Node(Data d) {
            data = d;
            left = null;
            right = null;
            parent = null;
        }
    }

    // -------------------- removeNode( Node ) ------------------------
    /**
     * Remove a specific node from the tree.
     * 
     * @param n Node node to be deleted
     */
    void removeNode(Node n) {
        if (root == n) { // n is the root
            removeRoot();
        } else if (n.parent.left == n) {
            removeLeft(n.parent, n);
        } else {
            removeRight(n.parent, n);
        }
        size--;
    }

    /////////////////////////////////////////////////////////////////
    // DO NOT CHANGE ANY CODE PRIOR TO THIS POINT
    /////////////////////////////////////////////////////////////////
    // ---------------------- addToFarRight( node, node ) ----------------
    /**
     * Add subtree Node as the right most descendant of the 1st argument.
     * 
     * @param n       Node root of tree to which subtree must be added
     * @param subtree Node subtree to be added to far right of subtree
     */
    public void addToFarRight(Node n, Node subtree) {
        Node cur = n;
        while (cur.right != null) {
            cur = cur.right;
        }

        cur.right = subtree;
        subtree.parent = cur;
    }

    // ----------------------- addToFarLeft( Node, Node ) --------------
    /**
     * Add subtree Node as the left most descendant of the 1st argument.
     * 
     * @param n       Node root of tree to which subtree must be added
     * @param subtree Node subtree to be added to far left of subtree
     */
    public void addToFarLeft(Node n, Node subtree) {
        Node cur = n;
        while (cur.left != null) {
            cur = cur.left;
        }

        cur.left = subtree;
        subtree.parent = cur;
    }

    // -------------------- removeRight( Node, Node ) -------------------
    /**
     * Remove a node that is the right child of its parent.
     * 
     * @param parent Node
     * @param n      Node
     */
    private void removeRight(Node parent, Node n) {
        if (n.left != null && n.right != null) {
            this.addToFarLeft(n.right, n.left);
            parent.right = n.right;
            n.right.parent = parent;
            return;
        }

        if (n.left != null) {
            parent.right = n.left;
            n.left.parent = parent;
            return;
        }

        if (n.right != null) {
            parent.right = n.right;
            n.right.parent = parent;
            return;
        }

        parent.right = null;
    }

    // -------------------- removeLeft( Node, Node ) --------------------
    /**
     * Remove a node that is the left child of its parent.
     * 
     * @param parent Node
     * @param n      Node
     */
    private void removeLeft(Node parent, Node n) {        
        if (n.left != null && n.right != null) {
            this.addToFarRight(n.left, n.right);
            parent.left = n.left;
            n.left.parent = parent;
            return;
        }

        if (n.left != null) {
            parent.left = n.left;
            n.left.parent = parent;
            return;
        }

        if (n.right != null) {
            parent.left = n.right;
            n.right.parent = parent;
            return;
        }

        parent.left = null;
    }

    // -------------------- removeRoot( ) ------------------------------
    /**
     * Remove the root node.
     */
    private void removeRoot() {
        if (root.left != null && root.right != null) {
            this.addToFarLeft(root.right, root.left);
            root = root.right;
            root.parent = null;
            return;
        }

        if (root.left != null) {
            root = root.left;
            root.parent = null;
            return;
        }

        if (root.right != null) {
            root = root.right;
            root.parent = null;
            return;
        }

        root = null;
    }

    /////////////////////////////////////////////////////////////////
    // DO NOT CHANGE ANY CODE AFTER TO THIS POINT
    /////////////////////////////////////////////////////////////////

    // -------------------- delete( Data ) ------------------------------
    /**
     * Find the node in the tree whose data field contains a key that
     * matches the key contained in the Data parameter.
     * 
     * @param d Data
     * @return Data
     */
    public Data delete(Data d) {
        return delete(d.key);
    }

    // -------------------- delete( String ) ----------------------------
    /**
     * Find the node in the tree whose data field contains a key that
     * matches the string passed as an argument.
     * 
     * @param k String
     * @return Data
     */
    public Data delete(String k) {
        Data foundData = null;
        Node found = findNode(k);
        if (found != null) {
            foundData = found.data;
            removeNode(found);
        }
        return foundData;
    }

    // --------------------- root() ----------------------------------
    /**
     * Return root of the tree; this is package access so that DrawPanel
     * can do a prefix walk of the tree. Would be better to have multiple
     * iterators.
     * 
     * @return BinarySearchTree.Node
     */
    BinarySearchTree.Node root() {
        return root;
    }

    // -------------------- find( String ) -------------------------
    /**
     * Given a key value, search the tree to find the node that has
     * that key value, if it exists.
     *
     * Return the Data object from the node or null
     * 
     * @param key String
     * @return Data
     */
    public Data find(String key) {
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
        return found;
    }

    // -------------------- findNode( String ) -------------------------
    /**
     * Given a key value, search the tree to find the node that has
     * that key value, if it exists.
     *
     * Return the Data object from the node or null.
     * 
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

    // --------------------- add -----------------------------------
    /**
     * Add a node to the tree in its proper position determined by the
     * "key" field of the Data object. This method uses the addNode
     * recursive utility method.
     * 
     * @param data Data
     */
    public void add(Data data) {
        Node newNode = new Node(data);
        if (root == null) {
            root = newNode;
        } else {
            addNode(root, newNode);
        }
        size++;
    }

    // ------------------ addNode( Node, Node ) -----------------------
    /**
     * A recursive method to add a new Node (2nd argument) to the subtree
     * rooted at the first argument.
     * 
     * @param parent Node root of tree into which the new Node must go.
     * @param newOne Node Node to be added
     */
    private void addNode(Node parent, Node newOne) {
        if (newOne.data.compareTo(parent.data) < 0) {
            if (parent.left != null) {
                addNode(parent.left, newOne);
            } else {
                parent.left = newOne;
                newOne.parent = parent;
            }
        } else {
            if (parent.right != null) {
                addNode(parent.right, newOne);
            } else {
                parent.right = newOne;
                newOne.parent = parent;
            }
        }
    }

    // -------------------------- size() -------------------------
    /**
     * Return tree size.
     * 
     * @return int
     */
    public int size() {
        return size;
    }

    // -------------------------- toString() -------------------------
    /**
     * Generate a string representation of the tree.
     * 
     * @return String
     */
    public String toString() {
        return toString(root, "  ", "  ");
    }

    /**
     * Recursively generate a string representation for a Node of a tree.
     * indent is increased for increasing depth.
     * branch is a short character string that prefixes each node indicating
     * whether this node is a left (L) or right (R) child of its parent.
     * 
     * @param n      Node subtree root
     * @param indent String
     * @param branch String
     * @return String
     */
    private String toString(Node n, String indent, String branch) {
        String s = "";
        if (n != null) {
            String prefix = indent.substring(0, indent.length() - 2) + branch;
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
}