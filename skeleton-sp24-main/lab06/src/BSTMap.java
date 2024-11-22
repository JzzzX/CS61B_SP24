/** CS61B Lab06 BSTMap
 * @author Guinsoo
 */

import javax.crypto.KEM;
import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;
    private int size;

    private class Node{
        private K key;
        private V value;
        private Node left;
        private Node right;
        private int size;

        public Node(K k, V v) {
            key = k;
            value = v;
            size = 1;
        }
    }

    /**
     * Puts a value/key into the BSTMap
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return;
        }
        root = put(root, key, value);
    }
    /**
     * Helper for put
     */
    private Node put(Node node, K key, V value) {
        // recursion base case
        if (node == null) {
            size += 1;
            return new Node(key, value);
        }

        // compare current node and the put key
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        return node;
    }

    /**
     * Return the value for the input key from this BSTMap
     */
    @Override
    public V get (K key) {
        return get(root, key);
    }
    /**
     * Helper for get
     */
    private V get(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    /**
     * Returns whether this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false; // solve the situation of null key
        }
        return getNode(root, key) != null;
    }
    /**
     * Helper for getNode
     * Return the node if found, null otherwise
     */
    private Node getNode(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return getNode(node.left, key);
        } else if (cmp > 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }

    /**
     * Returns the size of BSTMap
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes every mapping from this map.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new TreeSet<>();  // 使用TreeSet因为它能自动保持有序
        collectKeys(root, keys);
        return keys;
    }
    /**
     * Helper method that collects all keys using inorder traversal
     */
    private void collectKeys(Node node, Set<K> keys) {
        if (node == null) {
            return;
        }

        // 中序遍历的三个步骤：
        // 1. 递归遍历左子树
        collectKeys(node.left, keys);
        // 2. 处理当前节点（添加key到set中）
        keys.add(node.key);
        // 3. 递归遍历右子树
        collectKeys(node.right, keys);
    }

    /**
     * Delete the key-value pair (key, value) and return value.
     * @param key
     * @return null if key not in BSTMap. Otherwise, V value.
     */
    @Override
    public V remove(K key) {
        V value = get(key);
        if (value == null) {
            return null;
        }
        root = remove(root, key);
        size--;
        return value;
    }
    /**
     * Helper for remove
     */
    private Node remove(Node node, K key) {
        if (node == null) {
            return null;
        }

        // 比较key值决定往哪个方向继续查找
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            // key 小于当前节点，往左子树递归
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            // key 大于当前节点，往右子树递归
            node.right = remove(node.right, key);
        } else {
            // 找到要删除的节点，处理三种情况

            // 情况1：没有子节点或只有一个子节点
            if (node.right == null) {
                return node.left;
            }
            if (node.left == null) {
                return node.right;
            }

            // 情况2：有两个子节点
            // 找到右子树中最小的节点来替换当前节点
            Node minNode = findMin(node.right); // 找到后继节点
            // 复制后继节点的值
            node.key = minNode.key;
            node.value = minNode.value;
            // 删除原后继节点
            node.right = remove(node.right, minNode.key);
        }
        return node;
    }
    /**
     * Helper method for findMin
     */
    private Node findMin(Node node) {
        if (node.left == null) {
            return node;
        }
        return findMin(node.left);
    }


    /**
     * Returns an iterator over elements of type {@code T}.
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        // 创建一个队列来存储有序的key
        Queue<K> queue = new LinkedList<>();
        // 用中序遍历填充队列
        inorder(root, queue);
        // 返回队列的迭代器
        return queue.iterator();
    }
    /**
     * Helper method for Iterator
     * 使用中序遍历将BST中的key按顺序加入队列
     */
    private void inorder(Node node, Queue<K> queue) {
        if (node == null) {
            return;
        }
        // 处理左子树
        inorder(node.left, queue);
        // 再处理当前节点
        queue.add(node.key);
        // 最后处理右子树
        inorder(node.right, queue);
    }
}
