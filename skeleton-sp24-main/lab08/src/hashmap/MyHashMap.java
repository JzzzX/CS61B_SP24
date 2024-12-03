package hashmap;

import edu.princeton.cs.algs4.SET;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Guinsoo
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadFactor;
    private int initialCapacity;

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.size = 0;

        this.buckets = new Collection[initialCapacity];

        for (int i = 0; i < initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /** Helper method to validate key and get bucket index */
    private int getBucketIndex(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed!");
        }
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        // Check if key already exists in the bucket
        for (Node node : buckets[bucketIndex]) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            }
        }

        // If key not found, add new node
        Node newNode = new Node(key, value);
        buckets[bucketIndex].add(newNode);
        size += 1;

        // Resize if loaded factor exceeded
        if ((double) size / buckets.length > loadFactor) {
            resize();
        }
    }
    /**
     * Helper method of resize
     */
    private void resize() {
        // Save old buckets array
        Collection<Node>[] oldBuckets = buckets;

        // Create new buckets array with double size
        buckets = new Collection[oldBuckets.length * 2];

        // Initialize new buckets
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }

        // Reset size
        size = 0;

        // Rehash and relocate all existing key-value pairs
        for (Collection<Node> oldBucket : oldBuckets) {
            for (Node node : oldBucket) {
                put(node.key, node.value);
            }
        }
    }


    @Override
    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        for (Node node : buckets[bucketIndex]) {
            if (key.equals(node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean containsKey(K key) {
        int bucketIndex = getBucketIndex(key);
        for (Node node : buckets[bucketIndex]) {
            if (key.equals(node.key)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void clear() {
        for (Collection<Node> bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();

        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                keys.add(node.key);
            }
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Collection<Node> bucket = buckets[bucketIndex];
        for (Node node : bucket) {
            if (key.equals(node.key)) {
                V value = node.value;
                bucket.remove(node);
                size -= 1;
                return value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
