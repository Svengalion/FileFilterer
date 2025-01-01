package file_filterer.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerStatisticsTest {

    private IntegerStatistics integerStatistics;

    @BeforeEach
    void setUp() {
        integerStatistics = new IntegerStatistics();
    }

    @Test
    void testUpdate_singleValue() {
        integerStatistics.update(42L);

        assertEquals(1, integerStatistics.getCount());
        assertEquals(42L, integerStatistics.getMin());
        assertEquals(42L, integerStatistics.getMax());
        assertEquals(42.0, integerStatistics.getSum());
        assertEquals(42.0, integerStatistics.getAvg());
    }

    @Test
    void testUpdate_multipleValues() {
        integerStatistics.update(10L);
        integerStatistics.update(20L);
        integerStatistics.update(30L);

        assertEquals(3, integerStatistics.getCount());
        assertEquals(10L, integerStatistics.getMin());
        assertEquals(30L, integerStatistics.getMax());
        assertEquals(60.0, integerStatistics.getSum());
        assertEquals(20.0, integerStatistics.getAvg());
    }

    @Test
    void testUpdate_negativeValues() {
        integerStatistics.update(-10L);
        integerStatistics.update(-20L);

        assertEquals(2, integerStatistics.getCount());
        assertEquals(-20L, integerStatistics.getMin());
        assertEquals(-10L, integerStatistics.getMax());
        assertEquals(-30.0, integerStatistics.getSum());
        assertEquals(-15.0, integerStatistics.getAvg());
    }

    @Test
    void testUpdate_noValues() {
        assertEquals(0.0, integerStatistics.getAvg());
        assertEquals(Long.MAX_VALUE, integerStatistics.getMin());
        assertEquals(Long.MIN_VALUE, integerStatistics.getMax());
        assertEquals(0.0, integerStatistics.getSum());
        assertEquals(0, integerStatistics.getCount());
    }

    @Test
    void testGetType() {
        assertEquals("int", integerStatistics.getType());
    }

    @Test
    void testGetTypeName() {
        assertEquals("Целые числа", integerStatistics.getTypeName());
    }

    @Test
    void testPrintFullStatistics() {
        integerStatistics.update(10L);
        integerStatistics.update(20L);

        var outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        integerStatistics.printStatistics(false, true);

        String expectedOutput = """
          Минимум: 10.0
          Максимум: 20.0
          Сумма: 30.0
          Среднее: 15.0
        """;
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);
    }
}
