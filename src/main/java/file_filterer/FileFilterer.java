package file_filterer;

import file_filterer.processor.LineProcessor;
import lombok.AllArgsConstructor;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@AllArgsConstructor
public class FileFilterer implements CommandLineRunner {
    private final ArgParser argParser;
    private final List<LineProcessor> processors;
    private final FileStatisticsProcessor fileStatisticsProcessor;

    private List<String> getArgsAndFiles(String[] args) {
        List<String> input = null;
        try {
            argParser.setFlags(args);
            input = argParser.getFiles();
            if (input.isEmpty()) {
                System.out.println("Добавьте файлы для изменения");
                System.exit(0);
            }
        } catch (ParseException e) {
            System.err.println("Ошибка парсинга аргументов " + e.getMessage());
        }
        return input;
    }

    public void processFiles(String[] args) {
        var files = getArgsAndFiles(args);

        for (String file : files) {
            Path path = Paths.get(file);
            if (!Files.exists(path)) {
                System.err.println("Файл " + file + " не существует");
                continue;
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processLine(line);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла " + file + ": " + e.getMessage());
            }
        }
        fileStatisticsProcessor.writeStatisticsToFile();
    }

    void processLine(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            return;
        }

        for (LineProcessor processor : processors) {
            if (processor.canProcess(line)) {
                processor.process(line);
                return;
            }
        }

        System.err.println("Не удалось обработать строку: " + line);
    }

    @Override
    public void run(String... args) {
        processFiles(args);
    }
}