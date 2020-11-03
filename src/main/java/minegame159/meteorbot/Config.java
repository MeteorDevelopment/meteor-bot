package minegame159.meteorbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String DISCORD_TOKEN;
    public static String MONGO_URL;
    public static String SERVER_TOKEN;

    public static void init() {
        try {
            Properties properties = new Properties();

            InputStream in = new FileInputStream("config.properties");
            properties.load(in);
            in.close();

            DISCORD_TOKEN = properties.getProperty("discord_token");
            MONGO_URL = properties.getProperty("mongo_url");
            SERVER_TOKEN = properties.getProperty("server_token");

            MeteorBot.LOG.info("Loaded config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
