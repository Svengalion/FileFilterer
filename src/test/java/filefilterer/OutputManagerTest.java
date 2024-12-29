package filefilterer;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OutputManagerTest {

    private Path tempDir;
    private OutputManager outputManager;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("OutputManagerTest");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testWriteInteger() throws IOException {
        outputManager = new OutputManager(tempDir.toString(), "prefix-", false);
        outputManager.writeInteger("100");
        outputManager.closeAll();

        Path intFile = tempDir.resolve("prefix-integers.txt");
        assertTrue(Files.exists(intFile));

        List<String> lines = Files.readAllLines(intFile);
        assertEquals(List.of("100"), lines);
    }

    @Test
    void testWriteFloat() throws IOException {
        outputManager = new OutputManager(tempDir.toString(), "prefix-", false);
        outputManager.writeFloat("123.45");
        outputManager.closeAll();

        Path floatFile = tempDir.resolve("prefix-floats.txt");
        assertTrue(Files.exists(floatFile));

        List<String> lines = Files.readAllLines(floatFile);
        assertEquals(List.of("123.45"), lines);
    }

    @Test
    void testWriteString() throws IOException {
        outputManager = new OutputManager(tempDir.toString(), "prefix-", false);
        outputManager.writeString("TestString");
        outputManager.closeAll();

        Path stringFile = tempDir.resolve("prefix-strings.txt");
        assertTrue(Files.exists(stringFile));

        List<String> lines = Files.readAllLines(stringFile);
        assertEquals(List.of("TestString"), lines);
    }

    @Test
    void testAppendMode() throws IOException {
        outputManager = new OutputManager(tempDir.toString(), "prefix-", true);
        outputManager.writeInteger("200");
        outputManager.closeAll();

        outputManager = new OutputManager(tempDir.toString(), "prefix-", true);
        outputManager.writeInteger("300");
        outputManager.closeAll();

        Path intFile = tempDir.resolve("prefix-integers.txt");
        assertTrue(Files.exists(intFile));

        List<String> lines = Files.readAllLines(intFile);
        assertEquals(List.of("200", "300"), lines);
    }

    @Test
    void testOverwriteMode() throws IOException {
        outputManager = new OutputManager(tempDir.toString(), "prefix-", false);
        outputManager.writeFloat("50.5");
        outputManager.closeAll();

        outputManager = new OutputManager(tempDir.toString(), "prefix-", false);
        outputManager.writeFloat("75.75");
        outputManager.closeAll();

        Path floatFile = tempDir.resolve("prefix-floats.txt");
        assertTrue(Files.exists(floatFile));

        List<String> lines = Files.readAllLines(floatFile);
        assertEquals(List.of("75.75"), lines);
    }

    @Test
    void testCreateDirectories() throws IOException {
        Path nestedDir = tempDir.resolve("nested/dir");
        outputManager = new OutputManager(nestedDir.toString(), "nested-", false);
        outputManager.writeString("NestedString");
        outputManager.closeAll();

        Path stringFile = nestedDir.resolve("nested-strings.txt");
        assertTrue(Files.exists(stringFile));

        List<String> lines = Files.readAllLines(stringFile);
        assertEquals(List.of("NestedString"), lines);
    }

    @Test
    void testMultipleWrites() throws IOException {
        outputManager = new OutputManager(tempDir.toString(), "multi-", false);
        outputManager.writeInteger("1");
        outputManager.writeInteger("2");
        outputManager.writeFloat("3.14");
        outputManager.writeString("Test");
        outputManager.closeAll();

        Path intFile = tempDir.resolve("multi-integers.txt");
        Path floatFile = tempDir.resolve("multi-floats.txt");
        Path stringFile = tempDir.resolve("multi-strings.txt");

        List<String> intLines = Files.readAllLines(intFile);
        List<String> floatLines = Files.readAllLines(floatFile);
        List<String> stringLines = Files.readAllLines(stringFile);

        assertEquals(List.of("1", "2"), intLines);
        assertEquals(List.of("3.14"), floatLines);
        assertEquals(List.of("Test"), stringLines);
    }

    @Test
    void testNoPrefix() throws IOException {
        outputManager = new OutputManager(tempDir.toString(), "", false);
        outputManager.writeString("NoPrefix");
        outputManager.closeAll();

        Path stringFile = tempDir.resolve("strings.txt");
        assertTrue(Files.exists(stringFile));

        List<String> lines = Files.readAllLines(stringFile);
        assertEquals(List.of("NoPrefix"), lines);
    }
}
