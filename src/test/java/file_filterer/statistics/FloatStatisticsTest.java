package file_filterer.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloatStatisticsTest {

    private FloatStatistics floatStatistics;

    @BeforeEach
    void setUp() {
        floatStatistics = new FloatStatistics();
    }

    @Test
    void testUpdate_singleValue() {
        floatStatistics.update(5.5);
        assertEquals(5.5, floatStatistics.getMin());
        assertEquals(5.5, floatStatistics.getMax());
        assertEquals(5.5, floatStatistics.getSum());
        assertEquals(5.5, floatStatistics.getAvg());
        assertEquals(1, floatStatistics.getCount());
    }

    @Test
    void testUpdate_multipleValues() {
        floatStatistics.update(10.1);
        floatStatistics.update(2.2);
        floatStatistics.update(7.7);

        assertEquals(2.2, floatStatistics.getMin());
        assertEquals(10.1, floatStatistics.getMax());
        assertEquals(20.0, floatStatistics.getSum());
        assertEquals(6.666666666666667, floatStatistics.getAvg(), 0.0001);
        assertEquals(3, floatStatistics.getCount());
    }

    @Test
    void testUpdate_noValues() {
        assertEquals(Double.MAX_VALUE, floatStatistics.getMin());
        assertEquals(Double.NEGATIVE_INFINITY, floatStatistics.getMax());
        assertEquals(0.0, floatStatistics.getSum());
        assertEquals(0.0, floatStatistics.getAvg());
        assertEquals(0, floatStatistics.getCount());
    }

    @Test
    void testGetType() {
        assertEquals("float", floatStatistics.getType());
    }

    @Test
    void testGetTypeName() {
        assertEquals("Вещественные числа", floatStatistics.getTypeName());
    }
}
