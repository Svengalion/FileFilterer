package file_filterer;

import lombok.AllArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@AllArgsConstructor
public class OutputManager {
    private String outputPath;
    private String prefix;
    private boolean append;

    private String getFileName(String type) {
        String filename = prefix;
        switch (type) {
            case "int":
                filename += "integers.txt";
                break;
            case "float":
                filename += "floats.txt";
                break;
            case "string":
                filename += "strings.txt";
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип: " + type);
        }
        return filename;
    }

    public void writeInFile(String data, String type) {
        String filename = getFileName(type);

        try {
            Path path = Paths.get(outputPath, filename);
            Path parent = path.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE,
                    append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}