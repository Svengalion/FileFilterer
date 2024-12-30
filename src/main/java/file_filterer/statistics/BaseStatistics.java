package file_filterer.statistics;

import lombok.Getter;

@Getter
public abstract class BaseStatistics<T> implements Statistics<T> {
    protected long count = 0;

    @Override
    public void printStatistics(boolean shortStats, boolean fullStats) {
        if (getCount() > 0) {
            System.out.println(getTypeName() + ":");
            if (shortStats) {
                System.out.println("  Количество: " + getCount());
            }
            if (fullStats) {
                printFullStatistics();
            }
        }
    }

    public abstract String getType();

    protected abstract String getTypeName();

    protected abstract void printFullStatistics();
}