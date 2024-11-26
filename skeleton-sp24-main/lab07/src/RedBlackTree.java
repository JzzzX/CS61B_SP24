public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /**
         * Creates a RBTreeNode with item ITEM and color depending on ISBLACK
         * value.
         * @param isBlack
         * @param item
         */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /**
         * Creates a RBTreeNode with item ITEM, color depending on ISBLACK
         * value, left child LEFT, and right child RIGHT.
         * @param isBlack
         * @param item
         * @param left
         * @param right
         */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Creates an empty RedBlackTree.
     */
    public RedBlackTree() {
        root = null;
    }

    /**
     * Flips the color of node and its children. Assume that NODE has both left
     * and right children
     * @param node The node whose color needs to be flipped along with its children.
     */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack; // Flip the color of current node
        node.left.isBlack = !node.left.isBlack; // Flip the color of left child
        node.right.isBlack = !node.right.isBlack; // Flip the color of right child
    }

    /**
     * Rotates the given node to the right. Returns the new root node of
     * this subtree. For this implementation, make sure to swap the colors
     * of the new root and the old root!
     *
     * Example of right rotation:
     *       N            L
     *      /     =>    / \
     *     L           A   N
     *    /
     *   A
     *
     * @param node The root node of the subtree to be rotated
     * @return The new root node of the subtree after rotation (original left child)
     */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        // Save the new root (original left child)
        RBTreeNode<T> newRoot = node.left;
        // Move the new root's right subtree to the original root's left subtree
        node.left = newRoot.right;
        // Make the original root the right child of the new root
        newRoot.right = node;

        // Swap colors between the new and old roots
        boolean oldColor = newRoot.isBlack;
        newRoot.isBlack = node.isBlack;
        node.isBlack = oldColor;

        return newRoot;
    }

    /**
     * Rotates the given node to the lef    t. Returns the new root node of
     * this subtree. For this implementation, make sure to swap the colors
     * of the new root and the old root!
     *
     * Example of left rotation:
     *      N                R
     *     / \             / \
     *    A   R    =>     N   C
     *       / \         / \
     *      B   C       A   B
     *
     * @param node
     * @return
     */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;

        boolean oldColor = newRoot.isBlack;
        newRoot.isBlack = node.isBlack;
        node.isBlack = oldColor;

        return newRoot;
    }

    /**
     * Helper method that returns whether the given node is red. Null nodes (children or leaf
     * nodes) are automatically considered black.
     * @param node
     * @return
     */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    /**
     * Inserts the item into the Red Black Tree. Colors the root of the tree black.
     * @param item
     */
    public void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    /**
     * Inserts the given node into this Red Black Tree. Comments have been provided to help break
     * down the problem. For each case, consider the scenario needed to perform those operations.
     * Make sure to also review the other methods in this class!
     *
     * The balancing operations include:
     *   1. Left rotation when there's a red right child but black left child
     *   2. Right rotation when there are two consecutive red nodes on the left
     *   3. Color flip when both children are red
     *
     * @param node The root of the subtree where insertion takes place
     * @param item The item to be inserted
     * @return The root of the modified subtree after insertion and balancing
     */
    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        // Base BST insert
        if (node == null) {
            return new RBTreeNode<>(false, item);
        }

        int cmp = item.compareTo(node.item);
        if (cmp < 0) {
            node.left = insert(node.left, item);
        } else if (cmp > 0) {
            node.right = insert(node.right, item);
        }

        // Balance adjust
        // case 1 RR LB, rotate left
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        // case 2 LR LR, rotate right
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        // case 3 LR RR, color flip
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

}
