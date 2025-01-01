package file_filterer.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseStatisticsTest {

    private TestStatistics testStatistics;

    @BeforeEach
    void setUp() {
        testStatistics = new TestStatistics();
    }

    @Test
    void testPrintStatistics_shortStats() {
        testStatistics.update("value1");
        testStatistics.update("value2");

        assertDoesNotThrow(() -> testStatistics.printStatistics(true, false));
    }

    @Test
    void testPrintStatistics_fullStats() {
        testStatistics.update("value1");

        assertDoesNotThrow(() -> testStatistics.printStatistics(false, true));
    }

    @Test
    void testPrintStatistics_shortAndFullStats() {
        testStatistics.update("value1");
        testStatistics.update("value2");

        assertDoesNotThrow(() -> testStatistics.printStatistics(true, true));
    }

    @Test
    void testPrintStatistics_noUpdates() {
        assertDoesNotThrow(() -> testStatistics.printStatistics(true, true));
    }

    @Test
    void testGetType() {
        assertEquals("test", testStatistics.getType());
    }

    @Test
    void testGetTypeName() {
        assertEquals("Тестовые данные", testStatistics.getTypeName());
    }
}
