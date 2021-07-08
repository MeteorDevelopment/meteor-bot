package meteordevelopment.meteorbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import meteordevelopment.meteorbot.Config;
import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.database.documents.DbDailyStats;
import meteordevelopment.meteorbot.database.documents.DbMute;
import meteordevelopment.meteorbot.database.documents.DbTicket;
import org.bson.Document;

import java.util.Collections;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Database {
    public static Collection<DbDailyStats> DAILY_STATS;
    public static Collection<DbTicket> TICKETS;
    public static Collection<DbMute> MUTES;
    private static MongoDatabase database;

    public static void init() {
        MongoClient client = MongoClients.create(Config.MONGO_URL);
        database = client.getDatabase("meteor-bot");

        DAILY_STATS = new Collection<>("daily-stats-dev", DbDailyStats::new);
        TICKETS = new Collection<>("tickets-dev", DbTicket::new);
        MUTES = new Collection<>("mutes-dev", DbMute::new);

        MeteorBot.LOG.info("Connected to database");
    }

    public static MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    public static void addDonor(String discordId) {
        MongoCollection<Document> accounts = getCollection("accounts");

        Document account = accounts.find(eq("discord_id", discordId)).first();
        if (account == null) return;

        accounts.updateOne(eq("discord_id", discordId), set("donator", true));
        accounts.updateOne(eq("discord_id", discordId), set("can_have_custom_cape", true));

        String cape = account.getString("cape").equals("") ? "donator" : account.getString("cape");
        accounts.updateOne(eq("discord_id", discordId), set("cape", cape));

        boolean admin = account.getBoolean("admin", false);
        accounts.updateOne(eq("discord_id", discordId), set("max_mc_accounts", admin ? 5 : 1));

//        List<String> mcAccounts = account.getList("mc_accounts", String.class);
//        if (mcAccounts == null) return;
//
//        mcAccounts.forEach(uuid -> Unirest.post("http://pvp.meteorclient.com:8115/adddonor")
//                .queryString("token", Config.MPVP_TOKEN)
//                .queryString("uuid", uuid)
//                .asEmpty());
    }

    public static void removeDonor(String discordId) {
        MongoCollection<Document> accounts = getCollection("accounts");

        Document account = accounts.find(eq("discord_id", discordId)).first();
        if (account == null) return;

        accounts.updateOne(eq("discord_id", discordId), set("donator", false));
        accounts.updateOne(eq("discord_id", discordId), set("can_have_custom_cape", false));
        accounts.updateOne(eq("discord_id", discordId), set("cape", ""));
        accounts.updateOne(eq("discord_id", discordId), set("max_mc_accounts", 0));

        accounts.updateOne(eq("discord_id", discordId), set("mc_accounts", Collections.emptyList()));

//        List<String> mcAccounts = account.getList("mc_accounts", String.class);
//        if (mcAccounts == null) return;
//
//        mcAccounts.forEach(uuid -> Unirest.post("http://pvp.meteorclient.com:8115/removedonor")
//                .queryString("token", Config.MPVP_TOKEN)
//                .queryString("uuid", uuid)
//                .asEmpty());
    }
}