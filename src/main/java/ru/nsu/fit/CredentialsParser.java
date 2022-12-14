package ru.nsu.fit;

import java.io.IOException;
import java.util.Properties;

public class CredentialsParser {
    public static String getKey(String key) {
        Properties properties = new Properties();

        try {
            properties.load(CredentialsParser.class.getModule().getResourceAsStream("credentials.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty(key);
    }
}
