package file_filterer;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class OutputManagerTest {

    private OutputManager outputManager;
    private static final String TEST_OUTPUT_PATH = "test_output";
    private static final String PREFIX = "test_";

    @BeforeEach
    void setUp() {
        outputManager = new OutputManager();
    }

    @AfterEach
    void tearDown() throws IOException {
        Path path = Paths.get(TEST_OUTPUT_PATH);
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            System.err.println("Ошибка удаления файла: " + p);
                        }
                    });
        }
    }

    @Test
    void testWriteInFile_createsFileAndWritesData() throws IOException {
        String data = "Test data";
        String type = "string";
        boolean append = false;

        outputManager.writeInFile(data, type, TEST_OUTPUT_PATH, append, PREFIX);

        Path filePath = Paths.get(TEST_OUTPUT_PATH, PREFIX + "strings.txt");
        assertThat(Files.exists(filePath)).isTrue();

        String content = Files.readString(filePath);
        assertThat(content.trim()).isEqualTo(data);
    }

    @Test
    void testWriteInFile_appendsDataToFile() throws IOException {
        String data1 = "First line";
        String data2 = "Second line";
        String type = "int";
        boolean append = true;

        outputManager.writeInFile(data1, type, TEST_OUTPUT_PATH, false, PREFIX);
        outputManager.writeInFile(data2, type, TEST_OUTPUT_PATH, append, PREFIX);

        Path filePath = Paths.get(TEST_OUTPUT_PATH, PREFIX + "integers.txt");
        assertThat(Files.exists(filePath)).isTrue();

        String content = Files.readString(filePath);
        assertThat(content).isEqualTo(String.join(System.lineSeparator(), data1, data2) + System.lineSeparator());
    }

    @Test
    void testWriteInFile_handlesInvalidType() {
        String data = "Invalid type data";
        String type = "unknown";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            outputManager.writeInFile(data, type, TEST_OUTPUT_PATH, false, PREFIX);
        });
    }

    @Test
    void testWriteInFile_createsParentDirectories() throws IOException {
        String data = "Data with directories";
        String type = "float";

        outputManager.writeInFile(data, type, TEST_OUTPUT_PATH + "/nested/folder", false, PREFIX);

        Path filePath = Paths.get(TEST_OUTPUT_PATH, "nested/folder", PREFIX + "floats.txt");
        assertThat(Files.exists(filePath)).isTrue();

        String content = Files.readString(filePath);
        assertThat(content.trim()).isEqualTo(data);
    }

    @Test
    void testWriteInFile_handlesIOExceptionGracefully() {
        String data = "Data";
        String type = "string";
        String invalidPath = "/invalid_path";

        outputManager.writeInFile(data, type, invalidPath, false, PREFIX);

        Path filePath = Paths.get(invalidPath, PREFIX + "strings.txt");
        assertThat(Files.exists(filePath)).isFalse();
    }
}
