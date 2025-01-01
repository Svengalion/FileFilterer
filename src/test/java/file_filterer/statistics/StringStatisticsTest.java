package file_filterer.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringStatisticsTest {

    private StringStatistics stringStatistics;

    @BeforeEach
    void setUp() {
        stringStatistics = new StringStatistics();
    }

    @Test
    void testUpdate_singleValue() {
        stringStatistics.update("hello");

        assertEquals(1, stringStatistics.getCount());
        assertEquals(5, stringStatistics.getMinLength());
        assertEquals(5, stringStatistics.getMaxLength());
    }

    @Test
    void testUpdate_multipleValues() {
        stringStatistics.update("hello");
        stringStatistics.update("hi");
        stringStatistics.update("world");

        assertEquals(3, stringStatistics.getCount());
        assertEquals(2, stringStatistics.getMinLength());
        assertEquals(5, stringStatistics.getMaxLength());
    }

    @Test
    void testUpdate_emptyString() {
        stringStatistics.update("");
        stringStatistics.update("hello");

        assertEquals(2, stringStatistics.getCount());
        assertEquals(0, stringStatistics.getMinLength());
        assertEquals(5, stringStatistics.getMaxLength());
    }

    @Test
    void testGetType() {
        assertEquals("string", stringStatistics.getType());
    }

    @Test
    void testGetTypeName() {
        assertEquals("Строки", stringStatistics.getTypeName());
    }

    @Test
    void testPrintFullStatistics() {
        stringStatistics.update("hi");
        stringStatistics.update("world");

        var outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        stringStatistics.printStatistics(false, true);

        String expectedOutput = """
          Самая короткая строка: 2
          Самая длинная строка: 5
        """;
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);
    }
}
