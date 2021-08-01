package meteordevelopment.meteorbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String DISCORD_TOKEN;
    public static String DISCORD_SECRET;
    public static String MONGO_URL;
    public static String TOKEN;

    public static void init() {
        try {
            Properties properties = new Properties();

            InputStream in = new FileInputStream("private-config.properties");
            properties.load(in);
            in.close();

            DISCORD_TOKEN = properties.getProperty("discord_token");
            DISCORD_SECRET = properties.getProperty("discord_secret");
            MONGO_URL = properties.getProperty("mongo_url");
            TOKEN = properties.getProperty("token");

            MeteorBot.LOG.info("Loaded config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
