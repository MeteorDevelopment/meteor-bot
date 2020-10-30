package minegame159.meteorbot;

import com.mongodb.client.model.Filters;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.utils.Utils;
import org.bson.Document;

import java.util.List;

import static spark.Spark.*;

public class WebServer {
    private static String CAPE_OWNERS;
    private static String CAPES;

    public static void init() {
        port(8082);

        updateCapes();

        get("/capeowners", (request, response) -> CAPE_OWNERS);
        get("/capes", (request, response) -> CAPES);

        get("/togglediscord", (request, response) -> {
            MeteorBot.PROCESS_DISCORD_EVENTS = !MeteorBot.PROCESS_DISCORD_EVENTS;
            MeteorBot.LOG.info("Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS);
            return "Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS;
        });
    }

    public static void updateCapes() {
        StringBuilder sb = new StringBuilder();

        // CAPE OWNERS
        int i = 0;
        for (Document document : Db.USERS.getAll().filter(Filters.ne("cape", ""))) {
            List<String> uuids = document.getList("mcAccounts", String.class);

            if (!uuids.isEmpty()) {
                String cape = document.getString("cape");

                for (String uuid : uuids) {
                    if (i > 0) sb.append("\n");
                    sb.append(Utils.newUUID(uuid)).append(" ").append(cape);
                    i++;
                }
            }
        }

        CAPE_OWNERS = sb.toString();
        sb.setLength(0);

        // CAPES
        i = 0;
        for (Document document : Db.CAPES.getAll()) {
            if (i > 0) sb.append("\n");
            sb.append(document.getString("id")).append(" ").append(document.getString("url"));
            i++;
        }

        CAPES = sb.toString();
    }

    public static void close() {
        stop();
        awaitStop();
    }
}
