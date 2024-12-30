package file_filterer.processor;

import file_filterer.statistics.Statistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FloatProcessor implements LineProcessor {
    private final IntegerProcessor integerProcessor;
    private final Statistics<Double> floatStatistics;
    @Getter
    private final List<String> listOfFloats;

    @Override
    public boolean canProcess(String line) {
        try {
            Double.parseDouble(line);
            listOfFloats.add(line);
            return !integerProcessor.canProcess(line);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void process(String line) {
        Double value = Double.parseDouble(line);
        floatStatistics.update(value);
    }
}