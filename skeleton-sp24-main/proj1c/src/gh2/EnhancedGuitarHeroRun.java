package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Enhanced Guitar simulator with musical note display
 */
public class EnhancedGuitarHeroRun {
    private static final double CONCERT_A = 440.0;
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final int KEYBOARD_SIZE = KEYBOARD.length();
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    private static String currentNote = "";
    private static double currentFreq = 0.0;

    // 添加音名映射数组
    private static final String[] NOTE_NAMES = {
            "Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#", "Si"
    };

    public static void main(String[] args) {
        // Create guitar strings
        GuitarString[] strings = new GuitarString[KEYBOARD_SIZE];
        for (int i = 0; i < KEYBOARD_SIZE; i++) {
            double frequency = CONCERT_A * Math.pow(2, (i - 24.0) / 12.0);
            strings[i] = new GuitarString(frequency);
        }

        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();

        drawInterface(null);

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);

                if (index != -1) {
                    strings[index].pluck();
                    currentNote = String.valueOf(key);
                    currentFreq = CONCERT_A * Math.pow(2, (index - 24.0) / 12.0);

                    StdDraw.clear();
                    drawInterface(key);
                    StdDraw.show();
                }
            }

            double sample = 0.0;
            for (GuitarString string : strings) {
                sample += string.sample();
            }
            sample = Math.max(-0.8, Math.min(0.8, sample));

            StdAudio.play(sample);

            for (GuitarString string : strings) {
                string.tic();
            }
        }
    }

    // 获取音名的辅助方法
    private static String getNoteName(int index) {
        // 根据键位索引计算音名
        int noteIndex = (index + 9) % 12; // 调整以匹配A的位置
        return NOTE_NAMES[noteIndex];
    }

    private static void drawInterface(Character currentKey) {
        // 背景
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(WIDTH/2, HEIGHT/2, WIDTH/2, HEIGHT/2);

        // 标题
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        StdDraw.text(WIDTH/2, HEIGHT*0.85, "Try to play some guitar riff!");

        // 键位说明
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        StdDraw.text(WIDTH/2, HEIGHT*0.75, "Press keys to play musical notes (Do-Re-Mi...)");

        // 主旋律提示
        StdDraw.setFont(new java.awt.Font("Monospace", java.awt.Font.BOLD, 16));
        String[] riff = {
                "Demo riff for presentation:",
                "j k n k n n k j",
                "j k m k m m k j"
        };
        for (int i = 0; i < riff.length; i++) {
            if (i == 0) {
                StdDraw.setPenColor(StdDraw.BLACK);
            } else {
                StdDraw.setPenColor(StdDraw.DARK_GRAY);
            }
            StdDraw.text(WIDTH/2, HEIGHT*0.6 - (i * 25), riff[i]);
        }

        // 当前演奏的音符信息
        if (currentKey != null) {
            int index = KEYBOARD.indexOf(currentKey);
            String noteName = getNoteName(index);

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            StdDraw.text(WIDTH/2, HEIGHT*0.4, "Playing: " + currentKey);

            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            StdDraw.text(WIDTH/2, HEIGHT*0.35, "Note: " + noteName);

            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            StdDraw.text(WIDTH/2, HEIGHT*0.3,
                    String.format("Frequency: %.1f Hz", currentFreq));
        }

        // 底部提示
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        StdDraw.text(WIDTH/2, HEIGHT*0.15, "Created by Guinsoo From CS61B_proj1c");

        StdDraw.show();
    }
}