package filefilterer;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FileFiltererTest {

    private static Path tempDir;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeAll
    static void setup() throws IOException {
        tempDir = Files.createTempDirectory("FileFiltererTest");
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testProcessFiles_noFiles() {
        List<String> files = Collections.emptyList();
        OutputManager mockOutputManager = mock(OutputManager.class);
        Statistics mockStatistics = mock(Statistics.class);

        FileFilterer.processFiles(files, mockOutputManager, mockStatistics);

        verify(mockOutputManager, never()).writeInteger(any());
        verify(mockOutputManager, never()).writeFloat(any());
        verify(mockOutputManager, never()).writeString(any());
    }

    @Test
    void testProcessFiles_invalidFile() {
        List<String> files = Collections.singletonList("invalid.txt");
        OutputManager mockOutputManager = mock(OutputManager.class);
        Statistics mockStatistics = mock(Statistics.class);

        FileFilterer.processFiles(files, mockOutputManager, mockStatistics);

        verify(mockOutputManager, never()).writeInteger(any());
        verify(mockOutputManager, never()).writeFloat(any());
        verify(mockOutputManager, never()).writeString(any());
    }

    @Test
    void testProcessFiles_validFile() throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        Files.write(filePath, "123\nabc\n45.67".getBytes());

        List<String> files = Collections.singletonList(filePath.toString());
        OutputManager mockOutputManager = mock(OutputManager.class);
        Statistics mockStatistics = mock(Statistics.class);

        FileFilterer.processFiles(files, mockOutputManager, mockStatistics);

        verify(mockOutputManager).writeInteger("123");
        verify(mockOutputManager).writeString("abc");
        verify(mockOutputManager).writeFloat("45.67");

        verify(mockStatistics).updateIntStats(123L);
        verify(mockStatistics).updateStringStats("abc");
        verify(mockStatistics).updateFloatStats(45.67);
    }

    @Test
    void testMainWithValidFiles() throws IOException {
        Path file1 = tempDir.resolve("1.txt");
        Path file2 = tempDir.resolve("2.txt");
        Files.writeString(file1, "123\nHello\n45.67");
        Files.writeString(file2, "456\nWorld\n89.01");

        String[] args = {
                "-s",
                "-a",
                "-p", "sample-",
                "-o", tempDir.toString(),
                file1.toString(),
                file2.toString()
        };

        FileFilterer.main(args);

        Path intFile = tempDir.resolve("sample-integers.txt");
        Path floatFile = tempDir.resolve("sample-floats.txt");
        Path stringFile = tempDir.resolve("sample-strings.txt");

        assertTrue(Files.exists(intFile));
        assertTrue(Files.exists(floatFile));
        assertTrue(Files.exists(stringFile));

        List<String> intContent = Files.readAllLines(intFile);
        List<String> floatContent = Files.readAllLines(floatFile);
        List<String> stringContent = Files.readAllLines(stringFile);

        assertEquals(List.of("123", "456"), intContent);
        assertEquals(List.of("45.67", "89.01"), floatContent);
        assertEquals(List.of("Hello", "World"), stringContent);

        String output = outContent.toString();
        assertTrue(output.contains("Статистика:"));
        assertTrue(output.contains("Целые числа:"));
        assertTrue(output.contains("Количество: 2"));
        assertTrue(output.contains("Вещественные числа:"));
        assertTrue(output.contains("Количество: 2"));
        assertTrue(output.contains("Строки:"));
        assertTrue(output.contains("Количество: 2"));
    }

    @Test
    void testMainWithNonExistentFile() throws IOException {
        Path existent = tempDir.resolve("existent.txt");
        Files.writeString(existent, "789\nTest\n12.34");

        Path nonExistent = tempDir.resolve("nonexistent.txt");

        String[] args = {
                "-s",
                "-o", tempDir.toString(),
                "-p", "test-",
                existent.toString(),
                nonExistent.toString()
        };

        FileFilterer.main(args);

        Path intFile = tempDir.resolve("test-integers.txt");
        Path floatFile = tempDir.resolve("test-floats.txt");
        Path stringFile = tempDir.resolve("test-strings.txt");

        assertTrue(Files.exists(intFile));
        assertTrue(Files.exists(floatFile));
        assertTrue(Files.exists(stringFile));

        List<String> intContent = Files.readAllLines(intFile);
        List<String> floatContent = Files.readAllLines(floatFile);
        List<String> stringContent = Files.readAllLines(stringFile);

        assertEquals(List.of("789"), intContent);
        assertEquals(List.of("12.34"), floatContent);
        assertEquals(List.of("Test"), stringContent);

        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Файл " + nonExistent + " не существует"));

        String output = outContent.toString();
        assertTrue(output.contains("Статистика:"));
        assertTrue(output.contains("Целые числа:"));
        assertTrue(output.contains("Количество: 1"));
        assertTrue(output.contains("Вещественные числа:"));
        assertTrue(output.contains("Количество: 1"));
        assertTrue(output.contains("Строки:"));
        assertTrue(output.contains("Количество: 1"));
    }

    @Test
    void testMainWithEmptyFiles() throws IOException {
        Path empty1 = tempDir.resolve("empty1.txt");
        Path empty2 = tempDir.resolve("empty2.txt");
        Files.createFile(empty1);
        Files.createFile(empty2);

        String[] args = {
                "-s",
                "-o", tempDir.toString(),
                "-p", "empty-",
                empty1.toString(),
                empty2.toString()
        };

        FileFilterer.main(args);

        Path intFile = tempDir.resolve("empty-integers.txt");
        Path floatFile = tempDir.resolve("empty-floats.txt");
        Path stringFile = tempDir.resolve("empty-strings.txt");

        assertFalse(Files.exists(intFile));
        assertFalse(Files.exists(floatFile));
        assertFalse(Files.exists(stringFile));

        String output = outContent.toString();
        assertFalse(output.contains("Статистика:"));
    }
}
