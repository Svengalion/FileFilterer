package file_filterer.statistics;

import org.springframework.stereotype.Component;

@Component
public class IntegerStatistics extends NumericStatistics<Long> {
    public IntegerStatistics() {
        this.min = Long.MAX_VALUE;
        this.max = Long.MIN_VALUE;
    }

    @Override
    public String getType() {
        return "int";
    }

    @Override
    protected String getTypeName() {
        return "Целые числа";
    }

    @Override
    public void update(Long value) {
        count++;
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
        sum += value;
    }
}