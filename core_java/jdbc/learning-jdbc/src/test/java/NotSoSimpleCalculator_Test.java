import com.tharuni.lil.jdbc.NotSoSimpleCalculator;
import com.tharuni.lil.jdbc.NotSoSimpleCalculatorImpl;
import com.tharuni.lil.jdbc.SimpleCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class NotSoSimpleCalculator_Test {

    NotSoSimpleCalculator calc;

    // NEW IMPLEMENT
    SimpleCalculator mockNotSimpleCalc = mock(SimpleCalculator.class);

    @Mock
    SimpleCalculator mockSimpleCalc;
    NotSoSimpleCalculatorImpl notSimpleCalc = null;

    @Before
    public void init() {
        calc = new NotSoSimpleCalculatorImpl(mockSimpleCalc);

        // NEW IMPLEMENT
        notSimpleCalc = new NotSoSimpleCalculatorImpl(mockNotSimpleCalc);
    }

    @Test
    public void test_power() {
        //write your test here
        when(mockNotSimpleCalc.power(2, 2)).thenReturn(4);
        assertEquals(4, notSimpleCalc.power(2,2));
    }

    @Test
    public void test_abs() {
        // when(mockNotSimpleCalc.abs(-1)).thenReturn(1);
        assertEquals(1, notSimpleCalc.abs(-1));
    }

    @Test
    public void test_sqrt() {
        //write your test here
        // when(mockNotSimpleCalc.sqrt(2, 2)).thenReturn(4.0);
        assertEquals(2.0, notSimpleCalc.sqrt(4), 0.0001);
    }

}