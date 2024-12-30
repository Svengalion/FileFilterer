package file_filterer.stats;

public class IntStats extends NumericStats<Integer> {
    @Override
    protected String getTypeName() {
        return "Целые числа";
    }
}