package file_filterer.stats;

import lombok.Getter;

@Getter
public abstract class BaseStats <T> implements Stats <T> {
    protected long count;

    @Override
    public void printStats(boolean shortStats, boolean fullStats) {
        if (getCount() > 0) {
            System.out.println(getTypeName() + ":");
            if (shortStats) {
                System.out.println("  Количество: " + getCount());
            }
            if (fullStats) {
                printFullStats();
            }
        }
    }

    protected abstract String getTypeName();

    protected abstract void printFullStats();
}
