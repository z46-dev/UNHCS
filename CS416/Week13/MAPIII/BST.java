public class BST<T extends Comparable<T>> implements Tree<T> {
    public class Node implements Tree.Node<T> {
        T data;
        Node left;
        Node right;
        Node parent;
        Node(T data) {
            this.data = data;
            left = right = null;
        }

        public T getValue() {
            return data;
        }

        public void setValue(T value) {
            data = value;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public Node getParent() {
            return parent;
        }
    }
    
    private Node root;

    public BST() {
        root = null;
    }

    public Node getRoot() {
        return root;
    }

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

    public String toString() {
        return toString(root);
    }

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

    public void clear() {
        root = null;
    }

    public boolean contains(Object o) {
        return get(o) != null;
    }

    public boolean isEmpty() {
        return root == null;
    }

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

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }

        return 1 + size(node.left) + size(node.right);
    }
}
