package file_filterer;

import file_filterer.processor.FloatProcessor;
import file_filterer.processor.IntegerProcessor;
import file_filterer.processor.StringProcessor;
import file_filterer.statistics.FloatStatistics;
import file_filterer.statistics.IntegerStatistics;
import file_filterer.statistics.StringStatistics;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FileStatisticsProcessor {
    private final ArgParser argParser;
    private final OutputManager outputManager;
    private final IntegerProcessor integerProcessor;
    private final FloatProcessor floatProcessor;
    private final StringProcessor stringProcessor;
    private final IntegerStatistics integerStatistics;
    private final FloatStatistics floatStatistics;
    private final StringStatistics stringStatistics;

    void writeStatisticsToFile() {
        String integerResult = String.join("\n", integerProcessor.getListOfIntegers());
        String floatResult = String.join("\n", floatProcessor.getListOfFloats());
        String stringResult = String.join("\n", stringProcessor.getListOfStrings());

        outputManager.writeInFile(integerResult, integerStatistics.getType(),
                argParser.getOutputPath(), argParser.isAppend(), argParser.getPrefix());
        outputManager.writeInFile(floatResult, floatStatistics.getType(),
                argParser.getOutputPath(), argParser.isAppend(), argParser.getPrefix());
        outputManager.writeInFile(stringResult, stringStatistics.getType(),
                argParser.getOutputPath(), argParser.isAppend(), argParser.getPrefix());

        integerStatistics.printStatistics(argParser.isShortStat(), argParser.isFull());
        floatStatistics.printStatistics(argParser.isShortStat(), argParser.isFull());
        stringStatistics.printStatistics(argParser.isShortStat(), argParser.isFull());
    }
}