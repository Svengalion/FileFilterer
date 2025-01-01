package file_filterer.processor;

import file_filterer.statistics.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IntegerProcessorTest {

    @Mock
    private Statistics<Long> mockStatistics;

    private List<String> integerList;
    private IntegerProcessor integerProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        integerList = new ArrayList<>();
        integerProcessor = new IntegerProcessor(mockStatistics, integerList);
    }

    @Test
    void testCanProcess_validInteger() {
        String line = "12345";
        boolean result = integerProcessor.canProcess(line);

        assertTrue(result);
        assertTrue(integerList.contains(line));
    }

    @Test
    void testCanProcess_invalidInteger() {
        String line = "notANumber";
        boolean result = integerProcessor.canProcess(line);

        assertFalse(result);
        assertFalse(integerList.contains(line));
    }

    @Test
    void testCanProcess_floatNumber() {
        String line = "123.45";
        boolean result = integerProcessor.canProcess(line);

        assertFalse(result);
        assertFalse(integerList.contains(line));
    }

    @Test
    void testProcess_updatesStatistics() {
        String line = "6789";
        integerProcessor.process(line);

        verify(mockStatistics).update(6789L);
    }

    @Test
    void testProcess_invalidInteger_throwsException() {
        String line = "invalid";
        assertThrows(NumberFormatException.class, () -> integerProcessor.process(line));
    }

    @Test
    void testGetListOfIntegers_initiallyEmpty() {
        assertTrue(integerProcessor.getListOfIntegers().isEmpty());
    }

    @Test
    void testGetListOfIntegers_afterAddingIntegers() {
        integerProcessor.canProcess("123");
        integerProcessor.canProcess("456");

        List<String> list = integerProcessor.getListOfIntegers();

        assertEquals(2, list.size());
        assertTrue(list.contains("123"));
        assertTrue(list.contains("456"));
    }
}
