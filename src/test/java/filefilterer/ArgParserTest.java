package filefilterer;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ArgParserTest {

    @Test
    void testAllOptionsProvided() throws ParseException {
        String[] args = {"-o", "/output/path", "-p", "sample-", "-a", "-s", "-f", "file1.txt", "file2.txt"};
        ArgParser parser = new ArgParser(args);

        assertEquals("/output/path", parser.getOutputPath());
        assertEquals("sample-", parser.getPrefix());
        assertTrue(parser.isAppend());
        assertTrue(parser.isShort());
        assertTrue(parser.isFull());

        List<String> files = parser.getFiles();
        assertEquals(2, files.size());
        assertTrue(files.contains("file1.txt"));
        assertTrue(files.contains("file2.txt"));
    }

    @Test
    void testOnlyMandatoryOptions() throws ParseException {
        String[] args = {"file1.txt", "file2.txt"};
        ArgParser parser = new ArgParser(args);

        assertEquals(".", parser.getOutputPath());
        assertEquals("", parser.getPrefix());
        assertFalse(parser.isAppend());
        assertFalse(parser.isShort());
        assertFalse(parser.isFull());

        List<String> files = parser.getFiles();
        assertEquals(2, files.size());
        assertTrue(files.contains("file1.txt"));
        assertTrue(files.contains("file2.txt"));
    }

    @Test
    void testMissingArgumentForOption() {
        String[] args = {"-o", "-s", "file1.txt"};

        Exception exception = assertThrows(ParseException.class, () -> new ArgParser(args));

        String expectedMessage = "Missing argument for option: o";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidOption() {
        String[] args = {"-x", "value", "file1.txt"};

        Exception exception = assertThrows(ParseException.class, () -> new ArgParser(args));

        String expectedMessage = "Unrecognized option: -x";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testMultipleFlags() throws ParseException {
        String[] args = {"-s", "-a", "file1.txt", "file2.txt"};
        ArgParser parser = new ArgParser(args);

        assertTrue(parser.isShort());
        assertTrue(parser.isAppend());
        assertFalse(parser.isFull());
        assertEquals(".", parser.getOutputPath());
        assertEquals("", parser.getPrefix());

        List<String> files = parser.getFiles();
        assertEquals(2, files.size());
        assertTrue(files.contains("file1.txt"));
        assertTrue(files.contains("file2.txt"));
    }
}
