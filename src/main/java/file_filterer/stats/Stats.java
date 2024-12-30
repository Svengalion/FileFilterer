package file_filterer.stats;

public interface Stats<T> {
    void update(T value);
    long getCount();
    void printStats(boolean shortStats, boolean fullStats);
}
