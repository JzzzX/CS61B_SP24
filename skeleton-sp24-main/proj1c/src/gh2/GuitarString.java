package gh2;

import deque.Deque61B;
import deque.ArrayDeque61B;  // 或者使用 LinkedListDeque61B

//Note: This file will not compile until you complete the Deque61B implementations
public class GuitarString {
    /* 关键常量说明 */
    private static final int SR = 44100;      // SR（采样率）：每秒采集44100个样本，这是CD音质的标准采样率
    private static final double DECAY = .996;

    /* Buffer for storing sound data. */
    private Deque61B<Double> buffer;  // 用于存储声音数据的缓冲区

    /* 构造函数：GuitarString 的基本实现 */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        // 例如：如果frequency是440Hz（标准A音）
        // capacity = 44100/440 ≈ 100
        // 这100个点就模拟了弦上的100个位置

        buffer = new ArrayDeque61B<>();
        // 初始化缓冲区，初始状态：所有点都是0（静止的弦）
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int size = buffer.size();
        // 清空缓冲区 状态重置
        for (int i = 0; i < size; i++) {
            buffer.removeFirst();
        }

        // 填充随机噪声（白噪声模拟拨弦时的混乱状态）
        for (int i = 0; i < size; i++) {
            double r = Math.random() - 0.5; // 生成-0.5到0.5之间的随机数
            buffer.addLast(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        if (buffer.isEmpty()) {
            return;
        }

        // 获取当前位置的值（相当于弦的某个点）
        double first = buffer.removeFirst();
        // 获取下一个位置的值
        double second = buffer.get(0);
        // 计算新值（模拟能量传递和衰减）
        double newValue = DECAY * (first + second) / 2;
        // 将新值添加到缓冲区末尾以形成循环
        buffer.addLast(newValue);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
       if (buffer.isEmpty()) {
           return 0;
       }
       return buffer.get(0);
    }
}

