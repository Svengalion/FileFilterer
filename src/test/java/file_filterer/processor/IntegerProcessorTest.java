package file_filterer.processor;

import file_filterer.statistics.IntegerStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IntegerProcessorTest {

    @Mock
    private IntegerStatistics integerStatistics;
    @Autowired
    private IntegerProcessor integerProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        integerProcessor = new IntegerProcessor(integerStatistics, null);
    }

    @Test
    void testCanProcess_ValidInteger() {
        String validInteger = "123";
        assertTrue(integerProcessor.canProcess(validInteger));
    }

    @Test
    void testCanProcess_InvalidInteger() {
        String invalidInteger = "abc";
        assertFalse(integerProcessor.canProcess(invalidInteger));
    }

    @Test
    void testProcess_ValidInteger() {
        String validInteger = "123";
        integerProcessor.process(validInteger);

        verify(integerStatistics, times(1)).update(123L);
    }

    @Test
    void testProcess_InvalidInteger() {
        String invalidInteger = "abc";
        integerProcessor.canProcess(invalidInteger);

        verify(integerStatistics, times(0)).update(anyLong());
    }
}
