package file_filterer.statistics;

public class TestStatistics extends BaseStatistics<String> {
    @Override
    public void update(String value) {
    }

    @Override
    public String getType() {
        return "test";
    }

    @Override
    protected String getTypeName() {
        return "Тестовые данные";
    }

    @Override
    protected void printFullStatistics() {
        System.out.println("  Полная статистика тестовых данных");
    }
}
