import com.tharuni.lil.jdbc.SimpleCalculator;
import com.tharuni.lil.jdbc.SimpleCalculatorImpl;
import org.junit.*;

import static org.junit.Assert.assertEquals;


public class SimpleCalculator_Test {

    SimpleCalculator calculator = null;

    @Before
    public void init() {
        calculator = new SimpleCalculatorImpl();
    }

    @Test
    public void test_add() {
        int expected = 2;
        int actual = calculator.add(1, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void test_subtract() {
        //write your test here
        int expected = 1;
        int actual = calculator.subtract(2, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void test_multiply() {
        //write your test here
        int expected = 2;
        int actual = calculator.multiply(1, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void test_divide() {
        //write your test here
        double expected = 1.0;
        double actual = calculator.divide(2, 2);
        assertEquals(expected, actual, 0.0001);
    }

}