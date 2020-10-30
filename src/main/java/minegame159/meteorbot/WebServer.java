package minegame159.meteorbot;

import com.mongodb.client.model.Filters;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.utils.Utils;
import org.bson.Document;

import static spark.Spark.*;

public class WebServer {
    private static final String TOKEN = "omgpoggers159";

    private static String CAPE_OWNERS;
    private static String CAPES;

    public static void init() {
        port(8082);

        updateCapes();

        get("/capeowners", (request, response) -> CAPE_OWNERS);
        get("/capes", (request, response) -> CAPES);

        get("/togglediscord", (request, response) -> {
            String token = request.queryParams("token");
            if (token == null || !token.equals(TOKEN)) halt(401);

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
            User user = new User(document);
            if (!user.hasCape()) continue;

            if (!user.mcAccounts.isEmpty()) {
                String cape = user.cape;
                if (cape.equals("custom")) cape = user.id;

                for (String uuid : user.mcAccounts) {
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
