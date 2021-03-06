package minegame159.meteorbot.webserver.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import minegame159.meteorbot.Config;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import minegame159.meteorbot.database.documents.DailyStats;
import minegame159.meteorbot.database.documents.Stats;
import minegame159.meteorbot.json.UUIDSerializer;
import minegame159.meteorbot.utils.Utils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Updates.set;
import static spark.Spark.halt;

public class ApiController {
    private static final Logger LOG = LoggerFactory.getLogger("API");

    private static String CAPE_OWNERS;
    private static String CAPES;

    private static final Map<String, Long> PLAYING = new HashMap<>();
    //private static final Map<String, UUID> UUIDS = new HashMap<>();

    private static final Type UUID_LIST_TYPE = new TypeToken<List<UUID>>() {}.getType();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDSerializer())
            .create();

    public static Route HANDLE_VERSION = (request, response) -> Config.VERSION;

    public static Route HANDLE_CAPE_OWNERS = (request, response) -> {
        response.type("application/json");
        return CAPE_OWNERS;
    };

    public static Route HANDLE_CAPES = (request, response) -> {
        response.type("application/json");
        return CAPES;
    };

    public static Route HANDLE_TOGGLE_DISCORD = (request, response) -> {
        String token = request.queryParams("token");
        if (token == null || !token.equals(Config.SERVER_TOKEN)) halt(401);

        MeteorBot.PROCESS_DISCORD_EVENTS = !MeteorBot.PROCESS_DISCORD_EVENTS;
        MeteorBot.LOG.info("Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS);

        return "Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS;
    };

    public static Route HANDLE_SET_DEV_BUILD = (request, response) -> {
        String token = request.queryParams("token");
        if (token == null || !token.equals(Config.SERVER_TOKEN)) halt(401);

        String devBuild = request.queryParams("devBuild");
        if (devBuild != null) {
            MainController.DEV_BUILD = devBuild;

            Db.GLOBAL.update(Stats.ID, set("devBuild", devBuild));
        }

        response.type("text/plain");
        return "";
    };

    public static Route HANDLE_STATS = (request, response) -> {
        String date = request.queryParams("date");

        response.type("application/json");

        if (date != null) {
            // Daily stats
            DailyStats stats = Db.DAILY_STATS.get(date);

            if (stats == null) {
                response.status(404);
                return "{\"error\":\"Invalid date.\"}";
            }

            return GSON.toJson(stats);
        }

        // Global stats
        JsonObject o = new JsonObject();

        o.addProperty("version", Config.VERSION);
        o.addProperty("mcVersion", Config.MC_VERSION);
        o.addProperty("devBuild", MainController.DEV_BUILD);
        o.addProperty("downloads", MainController.DOWNLOADS);
        o.addProperty("onlinePlayers", PLAYING.size());
        //o.addProperty("onlineUuids", UUIDS.size());

        return GSON.toJson(o);
    };

    public static Route HANDLE_ONLINE_PING = (request, response) -> {
        String ip = Utils.getIp(request);
        //String uuid = request.queryParams("uuid");

        PLAYING.put(ip, System.currentTimeMillis());

        /*try {
            if (uuid == null) UUIDS.remove(ip);
            else UUIDS.put(ip, Utils.getUuid(uuid));
        } catch (IllegalArgumentException ignored) {
            LOG.warn("Received ping with invalid UUID '{}'", uuid);
        }*/

        response.type("text/plain");
        return "";
    };

    public static Route HANDLE_ONLINE_LEAVE = (request, response) -> {
        String ip = Utils.getIp(request);
        PLAYING.remove(ip);
        //UUIDS.remove(ip);

        response.type("text/plain");
        return "";
    };

    public static Route HANDLE_USING_METEOR = (request, response) -> {
        /*List<UUID> reqUuids;
        try {
            reqUuids = GSON.fromJson(request.body(), UUID_LIST_TYPE);
        } catch (Exception ignored) {
            reqUuids = null;
        }

        if (reqUuids == null) {
            response.status(400);
            return "";
        }

        Map<UUID, Boolean> resUuids = new HashMap<>(reqUuids.size());
        for (UUID uuid : reqUuids) resUuids.put(uuid, false);

        for (UUID uuid : UUIDS.values()) {
            for (UUID resUuid : resUuids.keySet()) {
                if (uuid.equals(resUuid)) resUuids.put(resUuid, true);
            }
        }

        response.type("application/json");
        return GSON.toJson(resUuids);*/

        response.type("application/json");
        return "{}";
    };

    public static int getOnlinePlayers() {
        return PLAYING.size();
    }

    public static void validateOnlinePlayers() {
        long time = System.currentTimeMillis();

        PLAYING.keySet().removeIf(s -> {
            if (time - PLAYING.get(s) > 6 * 60 * 1000) {
                //UUIDS.remove(s);
                return true;
            }

            return false;
        });
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
