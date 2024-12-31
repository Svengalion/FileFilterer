package file_filterer;

import file_filterer.processor.LineProcessor;
import file_filterer.statistics.FloatStatistics;
import file_filterer.statistics.IntegerStatistics;
import file_filterer.statistics.StringStats;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileFiltererTest {

    @Mock
    private ArgParser argParser;

    @Mock
    private LineProcessor lineProcessor;

    @Mock
    private FileStatisticsProcessor fileStatisticsProcessor;

    private FileFilterer fileFilterer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileFilterer = new FileFilterer(argParser, List.of(lineProcessor), fileStatisticsProcessor);
    }

    @Test
    void testProcessFiles_NoFilesProvided() throws ParseException {
        when(argParser.getFiles()).thenReturn(List.of());

        String[] args = {};
        fileFilterer.processFiles(args);

        verify(argParser).setFlags(args);
        verify(argParser).getFiles();
        verifyNoInteractions(fileStatisticsProcessor);
    }

    @Test
    void testProcessFiles_FileDoesNotExist() {
        String nonExistentFile = "non_existent_file.txt";
        when(argParser.getFiles()).thenReturn(List.of(nonExistentFile));

        fileFilterer.processFiles(new String[]{});

        verify(argParser).getFiles();
        verifyNoInteractions(fileStatisticsProcessor);
    }

    @Test
    void testProcessFiles_ValidFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "line1\nline2\n\nline3");

        when(argParser.getFiles()).thenReturn(List.of(tempFile.toString()));
        when(lineProcessor.canProcess(anyString())).thenReturn(true);

        fileFilterer.processFiles(new String[]{});

        verify(lineProcessor, times(3)).canProcess(anyString());
        verify(lineProcessor, times(3)).process(anyString());
        verify(fileStatisticsProcessor).writeStatisticsToFile();

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testProcessLine_EmptyLine() {
        fileFilterer.processFiles(new String[]{});
        fileFilterer.processLine("   ");

        verifyNoInteractions(lineProcessor);
    }

    @Test
    void testProcessLine_UnprocessableLine() {
        when(lineProcessor.canProcess("invalid_line")).thenReturn(false);

        fileFilterer.processLine("invalid_line");

        verify(lineProcessor).canProcess("invalid_line");
        verifyNoMoreInteractions(lineProcessor);
    }

    @Test
    void testProcessLine_ValidLine() {
        when(lineProcessor.canProcess("123"))
                .thenReturn(true);

        fileFilterer.processLine("123");

        verify(lineProcessor).canProcess("123");
        verify(lineProcessor).process("123");
    }

    @Test
    void testWriteStatisticsToFile() {
        fileFilterer.processFiles(new String[]{});
        fileStatisticsProcessor.writeStatisticsToFile();

        verify(fileStatisticsProcessor, times(1)).writeStatisticsToFile();
    }

    @Test
    void testErrorHandlingDuringFileRead() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        BufferedReader mockReader = mock(BufferedReader.class);
        when(Files.newBufferedReader(tempFile)).thenReturn(mockReader);
        when(mockReader.readLine()).thenThrow(new IOException("Test Exception"));

        fileFilterer.processFiles(new String[]{});

        verify(fileStatisticsProcessor, times(0)).writeStatisticsToFile();
        Files.deleteIfExists(tempFile);
    }
}