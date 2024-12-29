package filefilterer;

import org.apache.commons.cli.*;

import java.util.List;

public class ArgParser {
    private CommandLine cmd;

    public ArgParser(String[] args) throws ParseException {
        Options options;

        options = new Options();

        options.addOption("o", "output", true, "Изменение пути для результатов");
        options.addOption("p", "prefix", true, "Изменение префикса имен выходных файлов");
        options.addOption("a", "append", false, "Добавление информации в файлы без удаления");
        options.addOption("s", "short", false, "Краткая статистика");
        options.addOption("f", "full", false, "Полная статистика");

        CommandLineParser parser = new DefaultParser();
        cmd = parser.parse(options, args);
    }

    public String getOutputPath() {
        return cmd.getOptionValue("o", ".");
    }

    public String getPrefix() {
        return cmd.getOptionValue("p", "");
    }

    public boolean isAppend() {
        return cmd.hasOption("a");
    }

    public boolean isFull() {
        return cmd.hasOption("f");
    }

    public boolean isShort() {
        return cmd.hasOption("s");
    }

    public List<String> getFiles() {
        return cmd.getArgList();
    }
}
