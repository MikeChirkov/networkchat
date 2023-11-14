package ru.example;

import org.junit.jupiter.api.Test;
import ru.example.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilsTest {
    public static final String TEST_CONFIG_PATH = "src/test/resources/config.txt";
    public static final String TEST_LOG_PATH = "src/test/resources/log.txt";

    @Test
    void getConfig() throws IOException {
        Map<String, String> map = FileUtils.getConfig(TEST_CONFIG_PATH);
        assertEquals("TESTPORT", map.get("port"));
        assertEquals("TESTHOST", map.get("host"));
    }

    @Test
    void writeToFileLog() throws IOException {
        FileUtils.writeToFileLog("test1\n", TEST_LOG_PATH);
        FileUtils.writeToFileLog("test2\n", TEST_LOG_PATH);
        List<String> list = getLines();
        new File(TEST_LOG_PATH).deleteOnExit();
        assertEquals(2, list.size());
        assertEquals("test1", list.get(0));
        assertEquals("test2", list.get(1));
    }

    private List<String> getLines() {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TEST_LOG_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}