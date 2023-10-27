import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
//    /*
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void TestIsOffByOne () {
        boolean a = offByOne.equalChars('a', 'b');
        boolean b = offByOne.equalChars('c', 'd');
        boolean c = offByOne.equalChars('h', 'l');

        assertTrue(a);
        assertTrue(b);
        assertFalse(c);

    }
    // Your tests go here.
//    Uncomment this class once you've created your CharacterComparator interface and OffByOne class. **/
}
