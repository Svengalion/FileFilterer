package file_filterer.stats;

import lombok.Getter;

@Getter
public class StringStats extends BaseStats<String> {
    private long count;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;

    @Override
    public void update(String value) {
        count++;
        int length = value.length();
        if (length < minLength) {
            minLength = length;
        }
        if (length > maxLength) {
            maxLength = length;
        }
    }

    @Override
    protected String getTypeName() {
        return "Строки";
    }

    @Override
    protected void printFullStats() {
        System.out.println("  Самая короткая строка: " + minLength);
        System.out.println("  Самая длинная строка: " + maxLength);
    }
}