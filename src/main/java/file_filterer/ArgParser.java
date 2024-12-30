package file_filterer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@NoArgsConstructor
@Service
public class ArgParser {
    private String outputPath;
    private String prefix;
    private boolean append;
    private boolean full;
    private boolean shortStat;
    private List<String> files;

    public void setFlags(String[] args) throws ParseException {
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