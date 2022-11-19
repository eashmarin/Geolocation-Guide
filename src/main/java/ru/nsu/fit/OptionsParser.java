package ru.nsu.fit;

import java.io.IOException;
import java.util.Properties;

public class OptionsParser {
    public static int get(String optionKey) {
        Properties properties = new Properties();

        try {
            properties.load(OptionsParser.class.getModule().getResourceAsStream("request_options.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(properties.getProperty(optionKey));
    }
}
