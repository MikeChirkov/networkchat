package ru.example.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    public static Map<String, String> getConfig(String configPath) throws IOException {
        Map<String, String> config = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("port")) {
                    config.put("port", line.split("=")[1]);
                } else if (line.contains("host")) {
                    config.put("host", line.split("=")[1]);
                }
            }
        }
        return config;
    }

    public static void writeToFileLog(String message, String logPath) throws IOException {
        Path path = Paths.get(logPath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath, true))) {
            bw.write(message);
            bw.flush();
        }
    }

}
