package file_filterer.stats;

public class FloatStats extends NumericStats<Float> {
    @Override
    protected String getTypeName() {
        return "Вещественные числа";
    }
}