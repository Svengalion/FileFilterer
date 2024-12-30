package file_filterer;

import lombok.Getter;
import org.apache.commons.cli.*;

import java.util.List;

@Getter
public class ArgParser {
    private final String outputPath;
    private final String prefix;
    private final boolean append;
    private final boolean full;
    private final boolean shortStat;
    private final List<String> files;

    public ArgParser(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("o", "output", true, "Изменение пути для результатов");
        options.addOption("p", "prefix", true, "Изменение префикса имен выходных файлов");
        options.addOption("a", "append", false, "Добавление информации в файлы без удаления");
        options.addOption("s", "short", false, "Краткая статистика");
        options.addOption("f", "full", false, "Полная статистика");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        this.outputPath = cmd.getOptionValue("o", ".");
        this.prefix = cmd.getOptionValue("p", "");
        this.append = cmd.hasOption("a");
        this.full = cmd.hasOption("f");
        this.shortStat = cmd.hasOption("s");
        this.files = cmd.getArgList();
    }
}