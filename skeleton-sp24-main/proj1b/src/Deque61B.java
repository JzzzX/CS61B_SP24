package deque;

import java.util.ArrayList;
import java.util.List;

/**
 * Array-based implementation of Deque61B interface.
 */
public class ArrayDeque61B<T> implements Deque61B<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int INITIAL_CAPACITY = 8;
    private static final double MIN_USAGE_RATIO = 0.25;

    /** Creates an empty array deque. */
    public ArrayDeque61B() {
        items = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    /** Resizes the array to the target capacity. */
    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        int curr = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newItems[i] = items[curr];
            curr = plusOne(curr);
        }
        items = newItems;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    /** Returns the index immediately before the given index. */
    private int minusOne(int index) {
        return (index - 1 + items.length) % items.length;
    }

    /** Returns the index immediately after the given index. */
    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = x;
        nextFirst = minusOne(nextFirst);
        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        nextLast = plusOne(nextLast);
        size += 1;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int curr = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            returnList.add(items[curr]);
            curr = plusOne(curr);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = plusOne(nextFirst);
        T removed = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;

        if (items.length >= 16 && size < items.length * MIN_USAGE_RATIO) {
            resize(items.length / 2);
        }
        return removed;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = minusOne(nextLast);
        T removed = items[nextLast];
        items[nextLast] = null;
        size -= 1;

        if (items.length >= 16 && size < items.length * MIN_USAGE_RATIO) {
            resize(items.length / 2);
        }
        return removed;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        int curr = plusOne(nextFirst);
        for (int i = 0; i < index; i++) {
            curr = plusOne(curr);
        }
        return items[curr];
    }

    @Override
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return getRecursiveHelper(index, plusOne(nextFirst));
    }

    /** Helper method for getRecursive. */
    private T getRecursiveHelper(int index, int curr) {
        if (index == 0) {
            return items[curr];
        }
        return getRecursiveHelper(index - 1, plusOne(curr));
    }
}