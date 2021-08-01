package meteordevelopment.meteorbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import meteordevelopment.meteorbot.Config;
import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.database.documents.DbTicket;
import meteordevelopment.meteorbot.database.documents.Stats;
import org.bson.Document;

public class Db {
    private static MongoDatabase db;

    public static DbCollection<DbTicket> TICKETS;
    public static DbMultiCollection GLOBAL;

    public static void init() {
        MongoClient client = MongoClients.create(Config.MONGO_URL);
        db = client.getDatabase("meteor-bot");

        TICKETS = new DbCollection<>("tickets", DbTicket::new);
        GLOBAL = new DbMultiCollection("global");

        GLOBAL.register(Stats.class, Stats::new);

        MeteorBot.LOG.info("Connected to database");
    }

    static MongoCollection<Document> getCollection(String name) {
        return db.getCollection(name);
    }
}
