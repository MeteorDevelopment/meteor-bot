package minegame159.meteorbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.database.documents.JoinStats;
import minegame159.meteorbot.database.documents.User;
import org.bson.Document;

public class Db {
    private static MongoDatabase db;

    public static DbCollection<User> USERS;
    public static DbCollection<JoinStats> JOIN_STATS;
    public static DbCollection<Cape> CAPES;

    public static void init() {
        MongoClient client = MongoClients.create("mongodb+srv://admin:ASDjkl456@cluster0.9gg4z.mongodb.net/<dbname>?retryWrites=true&w=majority");
        db = client.getDatabase("meteor-bot");

        USERS = new DbCollection<>("users", User::new);
        JOIN_STATS = new DbCollection<>("join-stats", JoinStats::new);
        CAPES = new DbCollection<>("capes", Cape::new);

        MeteorBot.LOG.info("Connected to database");
    }

    static MongoCollection<Document> getCollection(String name) {
        return db.getCollection(name);
    }
}
