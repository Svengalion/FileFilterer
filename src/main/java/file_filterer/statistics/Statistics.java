package file_filterer.statistics;

public interface Statistics<T> {
    void update(T value);

    long getCount();

    void printStatistics(boolean shortStats, boolean fullStats);
}