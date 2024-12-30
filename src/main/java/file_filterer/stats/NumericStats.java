package file_filterer.stats;

import lombok.Getter;

@Getter
public abstract class NumericStats<T extends Number> extends BaseStats<T> {
    protected long count = 0;
    protected double min = Double.MAX_VALUE;
    protected double max = Double.MIN_VALUE;
    protected double sum = 0.0;

    public double getAvg() {
        return count > 0 ? sum / count : 0.0;
    }

    @Override
    public void update(T value) {
        count++;
        double val = value.doubleValue();
        if (val < min) {
            min = val;
        }
        if (val > max) {
            max = val;
        }
        sum += val;
    }

    @Override
    protected void printFullStats() {
        System.out.println("  Минимум: " + min);
        System.out.println("  Максимум: " + max);
        System.out.println("  Сумма: " + sum);
        System.out.println("  Среднее: " + getAvg());
    }
}