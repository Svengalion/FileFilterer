package file_filterer.processor;

import file_filterer.statistics.Statistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class IntegerProcessor implements LineProcessor {
    private final Statistics<Long> integerStatistics;
    @Getter
    private final List<String> listOfIntegers;

    @Override
    public boolean canProcess(String line) {
        try {
            Long.parseLong(line);
            listOfIntegers.add(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void process(String line) {
        Long value = Long.parseLong(line);
        integerStatistics.update(value);
    }
}