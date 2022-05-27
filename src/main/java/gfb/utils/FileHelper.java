package gfb.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileHelper {

    private FileHelper() {
        throw new IllegalStateException("FileHelper class");
    }

    /*This method reads and loads the file that get pass through.
     * It returns a HashMap with the content of the file*/
    public static Map<String, String> readPropertiesFile(String propertiesFile) {
        HashMap<String, String> propertiesData = new HashMap<>();
        try (InputStream input = new FileInputStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(input);
            for (String name : properties.stringPropertyNames()) {
                propertiesData.put(name, properties.getProperty(name));
            }
            System.out.println(new StringBuilder("Successfully loaded the ").append(propertiesFile).append(" file").toString());
        } catch (IOException e) {
            String errorMessage = new StringBuilder("There was an error reading the properties file - ").append(e.getMessage()).toString();
            System.err.println(errorMessage);
        }
        return propertiesData;
    }
}
