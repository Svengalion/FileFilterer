package file_filterer.processor;

import file_filterer.statistics.Statistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class StringProcessor implements LineProcessor {
    private final Statistics<String> stringStatistics;
    @Getter
    private List<String> listOfStrings;

    @Override
    public boolean canProcess(String line) {
        try {
            Long.parseLong(line);
            return false;
        } catch (NumberFormatException ignored) {
        }

        try {
            Double.parseDouble(line);
            return false;
        } catch (NumberFormatException ignored) {
        }
        listOfStrings.add(line);
        return true;
    }

    @Override
    public void process(String line) {
        stringStatistics.update(line);
    }
}