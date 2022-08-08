package org.meteordev.meteorbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String DISCORD_TOKEN;
    public static String BACKEND_TOKEN;
    public static String API_PATH;
    public static String UPTIME_URL;

    public static void init() {
        try {
            Properties properties = new Properties();

            InputStream in = new FileInputStream("private-config.properties");
            properties.load(in);
            in.close();

            DISCORD_TOKEN = properties.getProperty("discord_token");
            BACKEND_TOKEN = properties.getProperty("token");
            API_PATH = properties.getProperty("api_base");
            UPTIME_URL = properties.getProperty("uptime_url");

            MeteorBot.LOG.info("Loaded config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
