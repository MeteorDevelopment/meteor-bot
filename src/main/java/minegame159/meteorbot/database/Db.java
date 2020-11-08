package minegame159.meteorbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import minegame159.meteorbot.Config;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.database.documents.JoinStats;
import minegame159.meteorbot.database.documents.Stats;
import minegame159.meteorbot.database.documents.User;
import org.bson.Document;

public class Db {
    private static MongoDatabase db;

    public static DbCollection<User> USERS;
    public static DbCollection<JoinStats> JOIN_STATS;
    public static DbCollection<Cape> CAPES;
    public static DbMultiCollection GLOBAL;

    public static void init() {
        MongoClient client = MongoClients.create(Config.MONGO_URL);
        db = client.getDatabase("meteor-bot");

        USERS = new DbCollection<>("users", User::new);
        JOIN_STATS = new DbCollection<>("join-stats", JoinStats::new);
        CAPES = new DbCollection<>("capes", Cape::new);
        GLOBAL = new DbMultiCollection("global");

        GLOBAL.register(Stats.class, Stats::new);

        MeteorBot.LOG.info("Connected to database");
    }

    static MongoCollection<Document> getCollection(String name) {
        return db.getCollection(name);
    }
}
