package file_filterer.processor;

public interface LineProcessor {
    boolean canProcess(String line);

    void process(String line);
}