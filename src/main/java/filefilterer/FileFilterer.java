package filefilterer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileFilterer {

    public static void main(String[] args) {
        try {
            ArgParser argumentParser = new ArgParser(args);
            List<String> input = argumentParser.getFiles();
            if (input.isEmpty()) {
                System.out.println("No files selected");
                System.exit(0);
            }

            String outputPath = argumentParser.getOutputPath();
            String prefix = argumentParser.getPrefix();
            boolean append = argumentParser.isAppend();
            boolean shortStat = argumentParser.isShort();
            boolean fullStat = argumentParser.isFull();

            if (shortStat && fullStat) {
                shortStat = false;
            }

            OutputManager outputManager = new OutputManager(outputPath, prefix, append);

            Statistics statistics = new Statistics();

            for (String file : input) {
                Path path = Paths.get(file);
                if (!Files.exists(path)) {
                    System.err.println("Файл " + file + " не существует");
                    continue;
                }

                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        processLine(line, outputManager, statistics);
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при чтении файла " + file + ": " + e.getMessage());
                }
            }

            outputManager.closeAll();
            statistics.printStats(shortStat, fullStat);

        } catch (org.apache.commons.cli.ParseException e) {
            System.err.println("Ошибка парсинга аргументов " + e.getMessage());
        }
    }

    private static void processLine(String line, OutputManager outputManager, Statistics statistics) {
        line = line.trim();
        if (line.isEmpty()) {
            return;
        }

        if (isInt(line)) {
            outputManager.writeInteger(line);
            statistics.updateIntStats(Long.parseLong(line));
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
