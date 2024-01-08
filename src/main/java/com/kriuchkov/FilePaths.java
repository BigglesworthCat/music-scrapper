package com.kriuchkov;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilePaths {
    private static final Properties properties = new Properties();

    public static String MUSIC_SOURCES_PATH; // Path to file with music URLs
    public static String DOWNLOADS_PATH; // Path to directory where files will be downloaded

    static {
        try (InputStream input = FilePaths.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Unable to find application.properties");
                throw new RuntimeException("Cannot read properties!");
            }

            properties.load(input);

            MUSIC_SOURCES_PATH = properties.getProperty("music_sources");
            DOWNLOADS_PATH = properties.getProperty("downloads");
        } catch (IOException e) {
            System.out.println("Cannot read properties!");
        }
    }
}
