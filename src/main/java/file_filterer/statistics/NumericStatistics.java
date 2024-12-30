package file_filterer.statistics;

import lombok.Getter;

@Getter
public abstract class NumericStatistics<T extends Number> extends BaseStatistics<T> {
    protected long count = 0;
    protected double min = Double.MAX_VALUE;
    protected double max = Double.NEGATIVE_INFINITY;
    protected double sum = 0.0;

    public double getAvg() {
        return count > 0 ? sum / count : 0.0;
    }

    @Override
    protected void printFullStatistics() {
        System.out.println("  Минимум: " + min);
        System.out.println("  Максимум: " + max);
        System.out.println("  Сумма: " + sum);
        System.out.println("  Среднее: " + getAvg());
    }
}