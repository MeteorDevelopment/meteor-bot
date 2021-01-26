package minegame159.meteorbot.webserver.controllers;

import com.mongodb.client.model.Filters;
import minegame159.meteorbot.Config;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import minegame159.meteorbot.utils.Utils;
import org.bson.Document;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class ApiController {
    private static String CAPE_OWNERS;
    private static String CAPES;

    private static final Map<String, Long> PLAYING = new HashMap<>();

    public static Route HANDLE_VERSION = (request, response) -> Config.VERSION;
    public static Route HANDLE_CAPE_OWNERS = (request, response) -> CAPE_OWNERS;
    public static Route HANDLE_CAPES = (request, response) -> CAPES;

    public static Route HANDLE_TOGGLE_DISCORD = (request, response) -> {
        String token = request.queryParams("token");
        if (token == null || !token.equals(Config.SERVER_TOKEN)) halt(401);

        MeteorBot.PROCESS_DISCORD_EVENTS = !MeteorBot.PROCESS_DISCORD_EVENTS;
        MeteorBot.LOG.info("Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS);
        return "Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS;
    };

    public static Route HANDLE_ONLINE_PING = (request, response) -> {
        PLAYING.put(Utils.getIp(request), System.currentTimeMillis());
        return "";
    };

    public static Route HANDLE_ONLINE_LEAVE = (request, response) -> {
        PLAYING.remove(Utils.getIp(request));
        return "";
    };

    public static int getOnlinePlayers() {
        return PLAYING.size();
    }

    public static void validateOnlinePlayers() {
        long time = System.currentTimeMillis();
        PLAYING.values().removeIf(aLong -> time - aLong > 6 * 60 * 1000);
    }

    public static void updateCapes() {
        StringBuilder sb = new StringBuilder();

        // CAPE OWNERS
        int i = 0;
        for (Document document : Db.ACCOUNTS.getAll().filter(Filters.ne("cape", ""))) {
            Account account = new Account(document);
            if (!account.hasCape()) continue;

            if (!account.mcAccounts.isEmpty()) {
                String cape = account.cape;
                if (cape.equals("custom")) cape = account.id;

                for (String uuid : account.mcAccounts) {
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
}
