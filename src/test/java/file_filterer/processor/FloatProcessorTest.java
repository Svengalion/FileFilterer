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

class FloatProcessorTest {

    @Mock
    private IntegerProcessor mockIntegerProcessor;

    @Mock
    private Statistics<Double> mockStatistics;

    private List<String> floatList;
    private FloatProcessor floatProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        floatList = new ArrayList<>();
        floatProcessor = new FloatProcessor(mockIntegerProcessor, mockStatistics, floatList);
    }

    @Test
    void testCanProcess_validFloat() {
        String line = "123.45";

        when(mockIntegerProcessor.canProcess(line)).thenReturn(false);

        boolean result = floatProcessor.canProcess(line);

        assertTrue(result);
        assertTrue(floatList.contains(line));
    }

    @Test
    void testCanProcess_integerNotProcessedAsFloat() {
        String line = "123";

        when(mockIntegerProcessor.canProcess(line)).thenReturn(true);

        boolean result = floatProcessor.canProcess(line);

        assertFalse(result);
        assertFalse(floatList.contains(line));
    }

    @Test
    void testCanProcess_invalidFloat() {
        String line = "notANumber";

        boolean result = floatProcessor.canProcess(line);

        assertFalse(result);
        assertFalse(floatList.contains(line));
    }

    @Test
    void testProcess_updatesStatistics() {
        String line = "678.9";

        floatProcessor.process(line);

        verify(mockStatistics).update(678.9);
    }

    @Test
    void testProcess_invalidFloat_throwsException() {
        String line = "invalid";

        assertThrows(NumberFormatException.class, () -> floatProcessor.process(line));
    }

    @Test
    void testGetListOfFloats_initiallyEmpty() {
        assertTrue(floatProcessor.getListOfFloats().isEmpty());
    }

    @Test
    void testGetListOfFloats_afterAddingFloats() {
        floatProcessor.canProcess("123.45");
        floatProcessor.canProcess("678.90");

        List<String> list = floatProcessor.getListOfFloats();

        assertEquals(2, list.size());
        assertTrue(list.contains("123.45"));
        assertTrue(list.contains("678.90"));
    }
}
