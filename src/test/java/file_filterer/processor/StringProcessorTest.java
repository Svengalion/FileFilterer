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

class StringProcessorTest {

    @Mock
    private Statistics<String> mockStatistics;

    private List<String> stringList;
    private StringProcessor stringProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stringList = new ArrayList<>();
        stringProcessor = new StringProcessor(mockStatistics, stringList);
    }

    @Test
    void testCanProcess_stringLine() {
        String line = "Hello, world!";
        boolean result = stringProcessor.canProcess(line);

        assertTrue(result);
        assertTrue(stringList.contains(line));
    }

    @Test
    void testCanProcess_integerLine() {
        String line = "123";
        boolean result = stringProcessor.canProcess(line);

        assertFalse(result);
        assertFalse(stringList.contains(line));
    }

    @Test
    void testCanProcess_floatLine() {
        String line = "123.456";
        boolean result = stringProcessor.canProcess(line);

        assertFalse(result);
        assertFalse(stringList.contains(line));
    }

    @Test
    void testProcess_updatesStatistics() {
        String line = "Test string";
        stringProcessor.process(line);

        verify(mockStatistics).update(line);
    }

    @Test
    void testGetListOfStrings_initiallyEmpty() {
        assertTrue(stringProcessor.getListOfStrings().isEmpty());
    }

    @Test
    void testGetListOfStrings_afterAddingStrings() {
        stringProcessor.canProcess("First string");
        stringProcessor.canProcess("Second string");

        List<String> list = stringProcessor.getListOfStrings();

        assertEquals(2, list.size());
        assertTrue(list.contains("First string"));
        assertTrue(list.contains("Second string"));
    }
}
