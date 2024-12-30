package file_filterer;

import file_filterer.statistics.Statistics;
import lombok.AllArgsConstructor;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileFilterer {
    public static void main(String[] args) {
        try {
            ArgParser argumentParser = new ArgParser(args);
            List<String> input = argumentParser.getFiles();
            if (input.isEmpty()) {
                System.out.println("Добавьте файлы для изменения");
                System.exit(0);
            }

            var outputManager = new OutputManager(
                    argumentParser.getOutputPath(), argumentParser.getPrefix(), argumentParser.isAppend());

            processFiles(input, outputManager);
        } catch (ParseException e) {
            System.err.println("Ошибка парсинга аргументов " + e.getMessage());
        }
    }

    public static void processFiles(List<String> inputFiles, OutputManager outputManager) {
        for (String file : inputFiles) {
            Path path = Paths.get(file);
            if (!Files.exists(path)) {
                System.err.println("Файл " + file + " не существует");
                continue;
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processLine(line, outputManager);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла " + file + ": " + e.getMessage());
            }
        }
    }

    private static void processLine(String line, OutputManager outputManager, Statistics statistics) {
        var argParser = new ArgParser();

        line = line.trim();
        if (line.isEmpty()) {
            return;
        }

        if (isInt(line)) {
            statistics.printStatistics(argParser.isShortStat(), argParser.isFull());
        } else if (isFloat(line)) {
            outputManager.writeFloat(line);
            statistics.updateFloatStats(Double.parseDouble(line));
        } else {
            outputManager.writeString(line);
            statistics.updateStringStats(line);
        }
    }

    private static boolean isInt(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isFloat(String value) {
        try {
            Double.parseDouble(value);
            return !isInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}