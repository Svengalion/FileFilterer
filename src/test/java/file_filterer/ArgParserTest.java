package file_filterer;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ArgParserTest {

    private ArgParser argParser;

    @BeforeEach
    void setUp() {
        argParser = new ArgParser();
    }

    @Test
    void testDefaultValues() throws ParseException {
        String[] args = {};
        argParser.setFlags(args);

        assertEquals(".", argParser.getOutputPath());
        assertEquals("", argParser.getPrefix());
        assertFalse(argParser.isAppend());
        assertFalse(argParser.isFull());
        assertFalse(argParser.isShortStat());
        assertTrue(argParser.getFiles().isEmpty());
    }

    @Test
    void testCustomValues() throws ParseException {
        String[] args = {
                "-o", "output_path",
                "-p", "prefix_",
                "-a", "-f", "-s",
                "file1.txt", "file2.txt"
        };
        argParser.setFlags(args);

        assertEquals("output_path", argParser.getOutputPath());
        assertEquals("prefix_", argParser.getPrefix());
        assertTrue(argParser.isAppend());
        assertTrue(argParser.isFull());
        assertTrue(argParser.isShortStat());
        assertEquals(List.of("file1.txt", "file2.txt"), argParser.getFiles());
    }

    @Test
    void testInvalidOption() {
        String[] args = {"-invalid"};
        assertThrows(ParseException.class, () -> argParser.setFlags(args));
    }

    @Test
    void testMissingOptionArgument() {
        String[] args = {"-o"};
        assertThrows(ParseException.class, () -> argParser.setFlags(args));
    }
}
