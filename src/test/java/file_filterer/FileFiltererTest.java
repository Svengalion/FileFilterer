package file_filterer;

import file_filterer.processor.LineProcessor;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.*;

class FileFiltererTest {

    @Mock
    private ArgParser argParser;
    @Mock
    private FileStatisticsProcessor fileStatisticsProcessor;
    @Mock
    private LineProcessor lineProcessor;

    private FileFilterer fileFilterer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileFilterer = new FileFilterer(argParser, List.of(lineProcessor), fileStatisticsProcessor);
    }

    @Test
    void testProcessFiles_withValidFiles() throws ParseException, IOException {
        try (var mockedFiles = mockStatic(Files.class)) {
            doNothing().when(argParser).setFlags(any());
            when(argParser.getFiles()).thenReturn(List.of("file1.txt", "file2.txt"));

            mockedFiles.when(() -> Files.exists(Path.of("file1.txt"))).thenReturn(true);
            mockedFiles.when(() -> Files.exists(Path.of("file2.txt"))).thenReturn(true);

            BufferedReader reader1 = mock(BufferedReader.class);
            BufferedReader reader2 = mock(BufferedReader.class);

            mockedFiles.when(() -> Files.newBufferedReader(Path.of("file1.txt"))).thenReturn(reader1);
            mockedFiles.when(() -> Files.newBufferedReader(Path.of("file2.txt"))).thenReturn(reader2);

            when(reader1.readLine()).thenReturn("validLine1", (String) null);
            when(reader2.readLine()).thenReturn("validLine2", (String) null);

            when(lineProcessor.canProcess("validLine1")).thenReturn(true);
            when(lineProcessor.canProcess("validLine2")).thenReturn(true);

            fileFilterer.processFiles(new String[]{"--flag", "value"});

            verify(lineProcessor).process("validLine1");
            verify(lineProcessor).process("validLine2");
            verify(fileStatisticsProcessor).writeStatisticsToFile();
        }
    }


    @Test
    void testProcessFiles_withNonExistentFile() throws ParseException {
        try (var mockedFiles = mockStatic(Files.class)) {
            doNothing().when(argParser).setFlags(any());
            when(argParser.getFiles()).thenReturn(List.of("file1.txt", "file2.txt"));

            mockedFiles.when(() -> Files.exists(Path.of("file1.txt"))).thenReturn(false);
            mockedFiles.when(() -> Files.exists(Path.of("file2.txt"))).thenReturn(false);

            fileFilterer.processFiles(new String[]{"--flag", "value"});

            verify(fileStatisticsProcessor).writeStatisticsToFile();
            verify(lineProcessor, never()).process(anyString());
        }
    }

    @Test
    void testProcessLine_withProcessableLine() {
        when(lineProcessor.canProcess("processable")).thenReturn(true);

        fileFilterer.processLine("processable");

        verify(lineProcessor).process("processable");
    }

    @Test
    void testProcessLine_withUnprocessableLine() {
        when(lineProcessor.canProcess("unprocessable")).thenReturn(false);

        fileFilterer.processLine("unprocessable");

        verify(lineProcessor, never()).process("unprocessable");
    }
}
