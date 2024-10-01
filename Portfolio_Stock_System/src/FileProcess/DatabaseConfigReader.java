package FileProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConfigReader {
  public static Map<String, String> readDatabaseConfig(String filePath) throws IOException {
    Map<String, String> config = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(": ");
        if (parts.length == 2) {
          config.put(parts[0].trim(), parts[1].trim());
        }
      }
    }
    return config;
  }
}
