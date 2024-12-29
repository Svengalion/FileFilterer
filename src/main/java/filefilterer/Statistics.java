package filefilterer;

public class Statistics {

    IntegerStats integerStats;
    StringStats stringStats;
    FloatStats floatStats;

    public Statistics() {
        integerStats = new IntegerStats();
        stringStats = new StringStats();
        floatStats = new FloatStats();
    }

    public void updateIntStats(long value) {
        integerStats.update(value);
    }

    public void updateStringStats(String value) {
        stringStats.update(value);
    }

    public void updateFloatStats(double value) {
        floatStats.update(value);
    }

    public void printStats(boolean shortStats, boolean fullStats) {
        boolean hasStats = integerStats.getCount() > 0 || floatStats.getCount() > 0 || stringStats.getCount() > 0;

        if (hasStats) {
            System.out.println("Статистика:");
        }

        if (integerStats.getCount() > 0) {
            System.out.println("Целые числа:");
            if (shortStats) {
                System.out.println("  Количество: " + integerStats.getCount());
            }
            if (fullStats) {
                System.out.println("  Количество: " + integerStats.getCount());
                System.out.println("  Минимум: " + integerStats.getMin());
                System.out.println("  Максимум: " + integerStats.getMax());
                System.out.println("  Сумма: " + integerStats.getSum());
                System.out.println("  Среднее: " + integerStats.getAvg());
            }
        }

        if (floatStats.getCount() > 0) {
            System.out.println("Вещественные числа:");
            if (shortStats) {
                System.out.println("  Количество: " + floatStats.getCount());
            }
            if (fullStats) {
                System.out.println("  Количество: " + floatStats.getCount());
                System.out.println("  Минимум: " + floatStats.getMin());
                System.out.println("  Максимум: " + floatStats.getMax());
                System.out.println("  Сумма: " + floatStats.getSum());
                System.out.println("  Среднее: " + floatStats.getAvg());
            }
        }

        if (stringStats.getCount() > 0) {
            System.out.println("Строки:");
            if (shortStats) {
                System.out.println("  Количество: " + stringStats.getCount());
            }
            if (fullStats) {
                System.out.println("  Количество: " + stringStats.getCount());
                System.out.println("  Самая короткая строка: " + stringStats.getMinLength());
                System.out.println("  Самая длинная строка: " + stringStats.getMaxLength());
            }
        }
    }

    static class IntegerStats {
        private long count;
        private long min = Long.MAX_VALUE;
        private long max = Long.MIN_VALUE;
        private long sum = 0;
        private double avg = 0.0;

        public void update (long value) {
            count++;
            if (value < min) { min = value; }
            if (value > max) { max = value; }
            sum += value;
            avg = (double) sum / count;
        }

        public long getCount() { return count; }
        public long getMin() { return min; }
        public long getMax() { return max; }
        public long getSum() { return sum; }
        public double getAvg() { return avg; }
    }

    static class FloatStats {
        private long count = 0;
        private double min = Double.MAX_VALUE;
        private double max = Double.NEGATIVE_INFINITY;
        private double sum = 0.0;
        private double avg = 0.0;

        public void update(double value) {
            count++;
            if (value < min) { min = value; }
            if (value > max) { max = value; }
            sum += value;
            avg = sum / count;
        }

        public long getCount() { return count; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public double getSum() { return sum; }
        public double getAvg() { return avg; }
    }

    static class StringStats {
        private long count = 0;
        private int minLength = Integer.MAX_VALUE;
        private int maxLength = Integer.MIN_VALUE;

        public void update (String value) {
            count++;
            int length = value.length();
            if (length < minLength) { minLength = length; }
            if (length > maxLength) { maxLength = length; }
        }

        public long getCount() { return count; }
        public int getMinLength() { return minLength; }
        public int getMaxLength() { return maxLength; }
    }
}
