/** Second part of project1A.
 * deque implemented by array
 * @author JzzzX
 */

public class ArrayDeque<T> {
    // array to save data
    private T[] items;

    // size of the deque
    private int size;

    // size of the array
    private int length;

    //front index
    private int front;

    //rear index
    private int rear;

    /* constructor for array deque*/
    @SuppressWarnings("unchecked") // 抑制类型转换警告
    public ArrayDeque() {
        items = (T[]) new Object[8]; // 类型转换,用于创建泛型数组。
        size = 0;
        length = 8;
        front = 0;
        rear = 0;
    }

    /** decide if the deque is empty
     * @return true if the deque is empty,vice versa.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /* return the size of the deque*/
    public int size() {
        return size;
    }

    /** Resizing Array
    * resizing up and resizing down
    * */
    @SuppressWarnings("unchecked") // 抑制类型转换警告
    private void resize(int newLength) {
        T[] newItems = (T[]) new Object[newLength];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[(front + i) % length];
        }
        items = newItems;
        front = 0;
        rear = size;
        length = newLength;
    }

    /** add one item at the front of the deque.
     * before the add operation , check whether the item needs to resize up
     */
    public void addFirst(T item) {
        if (size == length) {
            resize(length * 2);
        }
        front = (front - 1 + length) % length; // 更新 front 指针, % length 模运算确保索引在数组范围内
        items[front] = item;
        size++;
    }

    /** add one item at the end of the deque
     * after the add operation , check whether the item needs to resize up
     */
    public void addLast(T item) {
        if (size == length) {
            resize(length * 2);
        }
        items[rear] = item;
        rear = (rear + 1) % length;
        size++;
    }

    /** remove one item at the front of the deque
     * after the delete operation , check whether the item needs to resize down
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = items[front]; // 获取要移除的元素
        items[front] = null; // 设置为 null，便于垃圾回收
        front = (front + 1) % length;
        size--;

        // 检查是否需要缩容
        if (length >= 16 && size < length / 4) {
            resize(length / 2);
        }
        return item; // 返回被移除的元素
    }

    /** remove one item at the end of the deque
     * after the delete operation , check whether the item needs to resize down
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        rear = (rear - 1 + length) % length;
        T item = items[rear];
        items[rear] = null;
        size--;

        if (length >= 16 && size < length / 4) {
            resize(length / 2);
        }
        return item;
    }

    /* return the item indexed at index. */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int actualIndex = (front + index) % length;
        return items[actualIndex];
    }


    /* print the entire deque from front to end. */
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println(); // 换行
    }

    /**
     * Create a deep copy of another ArrayDeque
     */
    @SuppressWarnings("unchecked")
    public ArrayDeque(ArrayDeque<T> other) {
        length = other.length;
        items = (T[]) new Object[length];
        size = other.size;
        front = 0;
        rear = size;
        for (int i = 0; i < size; i++) {
            items[i] = other.get(i);
        }
    }
}
