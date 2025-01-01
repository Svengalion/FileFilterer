package file_filterer;

import file_filterer.processor.FloatProcessor;
import file_filterer.processor.IntegerProcessor;
import file_filterer.processor.StringProcessor;
import file_filterer.statistics.FloatStatistics;
import file_filterer.statistics.IntegerStatistics;
import file_filterer.statistics.StringStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class FileStatisticsProcessorTest {

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
    private StringStatistics stringStatistics;

    private FileStatisticsProcessor fileStatisticsProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileStatisticsProcessor = new FileStatisticsProcessor(
                argParser, outputManager, integerProcessor, floatProcessor, stringProcessor,
                integerStatistics, floatStatistics, stringStatistics
        );
    }

    @Test
    void testWriteStatisticsToFile() {
        when(argParser.getOutputPath()).thenReturn("output");
        when(argParser.isAppend()).thenReturn(false);
        when(argParser.getPrefix()).thenReturn("test_");
        when(argParser.isShortStat()).thenReturn(true);
        when(argParser.isFull()).thenReturn(false);

        when(integerProcessor.getListOfIntegers()).thenReturn(java.util.List.of("1", "2", "3"));
        when(floatProcessor.getListOfFloats()).thenReturn(java.util.List.of("1.1", "2.2", "3.3"));
        when(stringProcessor.getListOfStrings()).thenReturn(java.util.List.of("A", "B", "C"));

        when(integerStatistics.getType()).thenReturn("int");
        when(floatStatistics.getType()).thenReturn("float");
        when(stringStatistics.getType()).thenReturn("string");

        fileStatisticsProcessor.writeStatisticsToFile();

        verify(outputManager).writeInFile("1\n2\n3", "int", "output", false, "test_");
        verify(outputManager).writeInFile("1.1\n2.2\n3.3", "float", "output", false, "test_");
        verify(outputManager).writeInFile("A\nB\nC", "string", "output", false, "test_");

        verify(integerStatistics).printStatistics(true, false);
        verify(floatStatistics).printStatistics(true, false);
        verify(stringStatistics).printStatistics(true, false);
    }
}
