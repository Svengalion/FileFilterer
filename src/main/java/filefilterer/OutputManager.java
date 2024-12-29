package filefilterer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class OutputManager {

    private String outputPath;
    private String prefix;
    private boolean append;
    private Map<String, BufferedWriter> writers;

    public OutputManager(String outputPath, String prefix, boolean append) {
        this.outputPath = outputPath;
        this.prefix = prefix;
        this.append = append;
        this.writers = new HashMap<>();
    }

    private BufferedWriter getWriter(String type) throws IOException {
        if (writers.containsKey(type)) {
            return writers.get(type);
        } else {
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

            Path path = Paths.get(outputPath, filename);
            Path parent = path.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE,
                    append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
            writers.put(type, writer);
            return writer;
        }
    }

    public void writeFloat(String data) {
        try {
            BufferedWriter writer = getWriter("float");
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи: " + e.getMessage());
        }
    }

    public void writeString(String data) {
        try {
            BufferedWriter writer = getWriter("string");
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи: " + e.getMessage());
        }
    }

    public void writeInteger(String data) {
        try {
            BufferedWriter writer = getWriter("int");
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи: " + e.getMessage());
        }
    }

    public void closeAll() {
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Ошибка закрытия файла: " + e.getMessage());
            }
        }
    }
}
