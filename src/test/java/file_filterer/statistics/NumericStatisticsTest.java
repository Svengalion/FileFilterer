package file_filterer.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericStatisticsTest {

    private NumericStatistics<Double> numericStatistics;

    @BeforeEach
    void setUp() {
        numericStatistics = new NumericStatistics<>() {
            @Override
            public void update(Double value) {
                count++;
                sum += value;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }

            @Override
            public String getType() {
                return "double";
            }

            @Override
            protected String getTypeName() {
                return "Числа";
            }
        };
    }

    @Test
    void testUpdate_singleValue() {
        numericStatistics.update(42.0);

        assertEquals(1, numericStatistics.getCount());
        assertEquals(42.0, numericStatistics.getMin());
        assertEquals(42.0, numericStatistics.getMax());
        assertEquals(42.0, numericStatistics.getSum());
        assertEquals(42.0, numericStatistics.getAvg());
    }

    @Test
    void testUpdate_multipleValues() {
        numericStatistics.update(10.0);
        numericStatistics.update(20.0);
        numericStatistics.update(30.0);

        assertEquals(3, numericStatistics.getCount());
        assertEquals(10.0, numericStatistics.getMin());
        assertEquals(30.0, numericStatistics.getMax());
        assertEquals(60.0, numericStatistics.getSum());
        assertEquals(20.0, numericStatistics.getAvg());
    }

    @Test
    void testUpdate_negativeValues() {
        numericStatistics.update(-10.0);
        numericStatistics.update(-20.0);

        assertEquals(2, numericStatistics.getCount());
        assertEquals(-20.0, numericStatistics.getMin());
        assertEquals(-10.0, numericStatistics.getMax());
        assertEquals(-30.0, numericStatistics.getSum());
        assertEquals(-15.0, numericStatistics.getAvg());
    }

    @Test
    void testGetAvg_noValues() {
        assertEquals(0.0, numericStatistics.getAvg());
    }

    @Test
    void testPrintFullStatistics() {
        numericStatistics.update(10.0);
        numericStatistics.update(20.0);

        var outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        numericStatistics.printStatistics(false, true);

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
