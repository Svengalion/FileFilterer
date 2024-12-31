package file_filterer;

import file_filterer.processor.IntegerProcessor;
import file_filterer.processor.FloatProcessor;
import file_filterer.processor.StringProcessor;
import file_filterer.statistics.IntegerStatistics;
import file_filterer.statistics.FloatStatistics;
import file_filterer.statistics.StringStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileStatisticsProcessorTest {

    @Mock
    private ArgParser argParser;

    @Mock
    private OutputManager outputManager;

    @Mock
    private IntegerProcessor integerProcessor;

    @Mock
    private FloatProcessor floatProcessor;

    @Mock
    private StringProcessor stringProcessor;

    @Mock
    private IntegerStatistics integerStatistics;

    @Mock
    private FloatStatistics floatStatistics;

    @Mock
    private StringStats stringStats;

    private FileStatisticsProcessor fileStatisticsProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileStatisticsProcessor = new FileStatisticsProcessor(argParser, outputManager, integerProcessor, floatProcessor, stringProcessor, integerStatistics, floatStatistics, stringStats);
    }

    @Test
    void testWriteStatisticsToFile() {
        when(integerProcessor.getListOfIntegers()).thenReturn(List.of("1", "2", "3"));
        when(floatProcessor.getListOfFloats()).thenReturn(List.of("1.1", "2.2", "3.3"));
        when(stringProcessor.getListOfStrings()).thenReturn(List.of("a", "b", "c"));

        fileStatisticsProcessor.writeStatisticsToFile();

        verify(outputManager, times(1)).writeInFile("1\n2\n3", "int", ".", false, "");
        verify(outputManager, times(1)).writeInFile("1.1\n2.2\n3.3", "float", ".", false, "");
        verify(outputManager, times(1)).writeInFile("a\nb\nc", "string", ".", false, "");
    }

    @Test
    void testWriteStatisticsToFile_EmptyLists() {
        when(integerProcessor.getListOfIntegers()).thenReturn(List.of());
        when(floatProcessor.getListOfFloats()).thenReturn(List.of());
        when(stringProcessor.getListOfStrings()).thenReturn(List.of());

        fileStatisticsProcessor.writeStatisticsToFile();

        verify(outputManager, never()).writeInFile(anyString(), anyString(), anyString(), anyBoolean(), anyString());
    }

    @Test
    void testWriteStatisticsToFile_NoData() {
        when(integerProcessor.getListOfIntegers()).thenReturn(List.of());
        when(floatProcessor.getListOfFloats()).thenReturn(List.of());
        when(stringProcessor.getListOfStrings()).thenReturn(List.of());

        fileStatisticsProcessor.writeStatisticsToFile();

        verify(outputManager, never()).writeInFile(anyString(), anyString(), anyString(), anyBoolean(), anyString());
    }
}
