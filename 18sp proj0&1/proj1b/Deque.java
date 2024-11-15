public interface Deque<T> {
    void addFirst(T item);
    void addLast(T item);
    T removeFirst();
    T removeLast();
    T get(int index);
    int size();
    boolean isEmpty();
    void printDeque();
}