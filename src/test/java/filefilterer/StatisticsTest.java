package filefilterer;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        statistics = new Statistics();
    }

    @Test
    void testUpdateIntStats() {
        statistics.updateIntStats(10);
        statistics.updateIntStats(20);
        statistics.updateIntStats(30);

        assertEquals(3, statistics.integerStats.getCount());
        assertEquals(10, statistics.integerStats.getMin());
        assertEquals(30, statistics.integerStats.getMax());
        assertEquals(60, statistics.integerStats.getSum());
        assertEquals(20.0, statistics.integerStats.getAvg());
    }

    @Test
    void testUpdateFloatStats() {
        statistics.updateFloatStats(1.5);
        statistics.updateFloatStats(2.5);
        statistics.updateFloatStats(3.5);

        assertEquals(3, statistics.floatStats.getCount());
        assertEquals(1.5, statistics.floatStats.getMin());
        assertEquals(3.5, statistics.floatStats.getMax());
        assertEquals(7.5, statistics.floatStats.getSum());
        assertEquals(2.5, statistics.floatStats.getAvg());
    }

    @Test
    void testUpdateStringStats() {
        statistics.updateStringStats("Hello");
        statistics.updateStringStats("World");
        statistics.updateStringStats("!");

        assertEquals(3, statistics.stringStats.getCount());
        assertEquals(1, statistics.stringStats.getMinLength());
        assertEquals(5, statistics.stringStats.getMaxLength());
    }

    @Test
    void testPrintStatsWithData() {
        statistics.updateIntStats(100);
        statistics.updateFloatStats(50.5);
        statistics.updateStringStats("Test");

        assertEquals(1, statistics.integerStats.getCount());
        assertEquals(100, statistics.integerStats.getMin());
        assertEquals(100, statistics.integerStats.getMax());
        assertEquals(100, statistics.integerStats.getSum());
        assertEquals(100.0, statistics.integerStats.getAvg());

        assertEquals(1, statistics.floatStats.getCount());
        assertEquals(50.5, statistics.floatStats.getMin());
        assertEquals(50.5, statistics.floatStats.getMax());
        assertEquals(50.5, statistics.floatStats.getSum());
        assertEquals(50.5, statistics.floatStats.getAvg());

        assertEquals(1, statistics.stringStats.getCount());
        assertEquals(4, statistics.stringStats.getMinLength());
        assertEquals(4, statistics.stringStats.getMaxLength());
    }

    @Test
    void testPrintStatsWithoutData() {
        assertDoesNotThrow(() -> statistics.printStats(false, false));
    }

    @Test
    void testIntegerStatsEdgeCases() {
        statistics.updateIntStats(Long.MAX_VALUE);
        statistics.updateIntStats(Long.MIN_VALUE);

        assertEquals(2, statistics.integerStats.getCount());
        assertEquals(Long.MIN_VALUE, statistics.integerStats.getMin());
        assertEquals(Long.MAX_VALUE, statistics.integerStats.getMax());
        assertEquals(Long.MAX_VALUE + Long.MIN_VALUE, statistics.integerStats.getSum());
        assertEquals((-0.5), statistics.integerStats.getAvg());
    }

    @Test
    void testFloatStatsEdgeCases() {
        statistics.updateFloatStats(Double.MAX_VALUE);
        statistics.updateFloatStats(Double.MIN_VALUE);

        assertEquals(2, statistics.floatStats.getCount());
        assertEquals(Double.MIN_VALUE, statistics.floatStats.getMin());
        assertEquals(Double.MAX_VALUE, statistics.floatStats.getMax());
        assertEquals(Double.MAX_VALUE + Double.MIN_VALUE, statistics.floatStats.getSum());
        assertEquals((Double.MAX_VALUE + Double.MIN_VALUE) / 2, statistics.floatStats.getAvg());
    }

    @Test
    void testStringStatsEdgeCases() {
        statistics.updateStringStats("");
        statistics.updateStringStats("A");

        assertEquals(2, statistics.stringStats.getCount());
        assertEquals(0, statistics.stringStats.getMinLength());
        assertEquals(1, statistics.stringStats.getMaxLength());
    }

    @Test
    void testStatisticsFullAndShortOutput() {
        statistics.updateIntStats(50);
        statistics.updateFloatStats(25.5);
        statistics.updateStringStats("Sample");

        assertDoesNotThrow(() -> statistics.printStats(true, true));
    }

    @Test
    void testStatisticsOnlyShortOutput() {
        statistics.updateIntStats(100);
        statistics.updateStringStats("Short");

        assertDoesNotThrow(() -> statistics.printStats(true, false));
    }

    @Test
    void testStatisticsOnlyFullOutput() {
        statistics.updateFloatStats(99.99);
        statistics.updateStringStats("FullStat");

        assertDoesNotThrow(() -> statistics.printStats(false, true));
    }

    @Test
    void testStatisticsNoUpdates() {
        assertDoesNotThrow(() -> statistics.printStats(false, false));
    }

    @Test
    void testStatisticsSingleUpdate() {
        statistics.updateIntStats(1);

        assertEquals(1, statistics.integerStats.getCount());
        assertEquals(1, statistics.integerStats.getMin());
        assertEquals(1, statistics.integerStats.getMax());
        assertEquals(1, statistics.integerStats.getSum());
        assertEquals(1.0, statistics.integerStats.getAvg());
    }

    @Test
    void testStatisticsNegativeNumbers() {
        statistics.updateIntStats(-10);
        statistics.updateIntStats(-20);
        statistics.updateFloatStats(-5.5);
        statistics.updateFloatStats(-15.5);

        assertEquals(2, statistics.integerStats.getCount());
        assertEquals(-20, statistics.integerStats.getMin());
        assertEquals(-10, statistics.integerStats.getMax());
        assertEquals(-30, statistics.integerStats.getSum());
        assertEquals(-15.0, statistics.integerStats.getAvg());

        assertEquals(2, statistics.floatStats.getCount());
        assertEquals(-15.5, statistics.floatStats.getMin());
        assertEquals(-5.5, statistics.floatStats.getMax());
        assertEquals(-21.0, statistics.floatStats.getSum());
        assertEquals(-10.5, statistics.floatStats.getAvg());
    }
}
