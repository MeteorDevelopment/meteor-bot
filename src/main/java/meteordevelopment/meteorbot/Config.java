package meteordevelopment.meteorbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String DISCORD_TOKEN, MPVP_TOKEN, MONGO_URL, VERSION, MC_VERSION;

    public static void init() {
        try {
            Properties properties = new Properties();

            InputStream in = new FileInputStream("private-config.properties");
            properties.load(in);
            in.close();

            DISCORD_TOKEN = properties.getProperty("discordToken");
            MPVP_TOKEN = properties.getProperty("mpvpToken");
            MONGO_URL = properties.getProperty("mongoUrl");
            VERSION = properties.getProperty("meteorVersion");
            MC_VERSION = properties.getProperty("mcVersion");

            MeteorBot.LOG.info("Loaded config");
        } catch (IOException e) {
            MeteorBot.LOG.info("Failed to load config");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
