package file_filterer.statistics;

import org.springframework.stereotype.Component;

@Component
public class FloatStatistics extends NumericStatistics<Double> {

    @Override
    public String getType() {
        return "float";
    }

    @Override
    protected String getTypeName() {
        return "Вещественные числа";
    }

    @Override
    public void update(Double value) {
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