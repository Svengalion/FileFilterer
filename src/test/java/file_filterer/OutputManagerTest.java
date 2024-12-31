package file_filterer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OutputManagerTest {

    @Mock
    private ArgParser argParser;

    @Mock
    private FileStatisticsProcessor fileStatisticsProcessor;

    private OutputManager outputManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        outputManager = new OutputManager();
    }

    @Test
    void testWriteInFile() throws IOException {
        String data = "test data";
        String type = "int";
        String outputPath = ".";
        String prefix = "test_";
        boolean append = false;

        Path outputPathDir = Paths.get(outputPath, "test_integers.txt");
        Files.createDirectories(outputPathDir.getParent());

        outputManager.writeInFile(data, type, outputPath, append, prefix);

        assertTrue(Files.exists(outputPathDir));
        String fileContent = Files.readString(outputPathDir);
        assertEquals("test data\n", fileContent);

        Files.deleteIfExists(outputPathDir);
    }

    @Test
    void testWriteInFile_Append() throws IOException {
        String data = "test data";
        String type = "int";
        String outputPath = ".";
        String prefix = "test_";
        boolean append = true;

        Path outputPathDir = Paths.get(outputPath, "test_integers.txt");
        Files.createDirectories(outputPathDir.getParent());
        Files.writeString(outputPathDir, "existing content\n");

        outputManager.writeInFile(data, type, outputPath, append, prefix);

        String fileContent = Files.readString(outputPathDir);
        assertEquals("existing content\ntest data\n", fileContent);

        Files.deleteIfExists(outputPathDir);
    }

    @Test
    void testWriteInFile_InvalidType() throws IOException {
        String data = "test data";
        String type = "invalid";
        String outputPath = ".";
        String prefix = "test_";
        boolean append = false;

        assertThrows(IllegalArgumentException.class, () -> outputManager.writeInFile(data, type, outputPath, append, prefix));
    }

    @Test
    void testCreateDirectories() throws IOException {
        String data = "test data";
        String type = "int";
        String outputPath = ".";
        String prefix = "test_";
        boolean append = false;

        Path outputPathDir = Paths.get(outputPath, "test_integers.txt");
        Path parentDir = outputPathDir.getParent();
        Files.createDirectories(parentDir);

        assertTrue(Files.exists(parentDir));

        Files.deleteIfExists(outputPathDir);
    }
}
