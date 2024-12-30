package file_filterer.statistics;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class StringStats extends BaseStatistics<String> {
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
    public String getType() {
        return "string";
    }

    @Override
    protected String getTypeName() {
        return "Строки";
    }

    @Override
    protected void printFullStatistics() {
        System.out.println("  Самая короткая строка: " + minLength);
        System.out.println("  Самая длинная строка: " + maxLength);
    }
}