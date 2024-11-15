import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    /*
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    Uncomment this class once you've created your CharacterComparator interface and OffByOne class. **/

    // 实例化 OffByOne 对象
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testEqualChars() {
        // 正向测试：相差 1 的字符应返回 true
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('r', 'q'));
        assertTrue(offByOne.equalChars('%', '&')); // 非字母字符

        // 反向测试：不相差 1 的字符应返回 false
        assertFalse(offByOne.equalChars('a', 'e'));
        assertFalse(offByOne.equalChars('z', 'a'));
        assertFalse(offByOne.equalChars('a', 'a')); // 相同字符
        assertFalse(offByOne.equalChars('A', 'b')); // 大小写敏感
    }
}
