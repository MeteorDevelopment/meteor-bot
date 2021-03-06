package minegame159.meteorbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import minegame159.meteorbot.Config;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.documents.*;
import org.bson.Document;

public class Db {
    private static MongoDatabase db;

    public static DbCollection<DailyStats> DAILY_STATS;
    public static DbCollection<Cape> CAPES;
    public static DbCollection<Account> ACCOUNTS;
    public static DbCollection<DbTicket> TICKETS;
    public static DbMultiCollection GLOBAL;

    public static void init() {
        MongoClient client = MongoClients.create(Config.MONGO_URL);
        db = client.getDatabase("meteor-bot");

        DAILY_STATS = new DbCollection<>("join-stats", DailyStats::new);
        CAPES = new DbCollection<>("capes", Cape::new);
        ACCOUNTS = new DbCollection<>("accounts-old", Account::new);
        TICKETS = new DbCollection<>("tickets", DbTicket::new);
        GLOBAL = new DbMultiCollection("global");

        GLOBAL.register(Stats.class, Stats::new);

        MeteorBot.LOG.info("Connected to database");
    }

    static MongoCollection<Document> getCollection(String name) {
        return db.getCollection(name);
    }
}
