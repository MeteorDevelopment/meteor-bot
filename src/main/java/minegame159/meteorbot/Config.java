package minegame159.meteorbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {
    // Private
    public static String DISCORD_TOKEN;
    public static String DISCORD_SECRET;
    public static String MONGO_URL;
    public static String SERVER_TOKEN;
    public static String PVP_SERVER_TOKEN;
    public static String GMAIL_PASSWORD;

    // Public
    public static String VERSION;
    public static String MC_VERSION;
    public static List<String> CHANGELOG;

    public static void init() {
        try {
            // Private config
            Properties properties = new Properties();

            InputStream in = new FileInputStream("private-config.properties");
            properties.load(in);
            in.close();

            DISCORD_TOKEN = properties.getProperty("discord_token");
            DISCORD_SECRET = properties.getProperty("discord_secret");
            MONGO_URL = properties.getProperty("mongo_url");
            SERVER_TOKEN = properties.getProperty("server_token");
            PVP_SERVER_TOKEN = properties.getProperty("pvp_server_token");
            GMAIL_PASSWORD = properties.getProperty("gmail_password");

            // Public config
            Reader reader = new FileReader("config.json");
            JsonObject json = (JsonObject) JsonParser.parseReader(reader);
            reader.close();

            VERSION = json.get("version").getAsString();
            MC_VERSION = json.get("mcVersion").getAsString();

            CHANGELOG = new ArrayList<>();
            for (JsonElement e : json.getAsJsonArray("changelog")) {
                CHANGELOG.add(e.getAsString());
            }

            MeteorBot.LOG.info("Loaded config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
