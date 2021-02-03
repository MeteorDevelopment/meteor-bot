package minegame159.meteorbot.webserver.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.mongodb.client.model.Filters;
import minegame159.meteorbot.Config;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import minegame159.meteorbot.json.UUIDSerializer;
import minegame159.meteorbot.json.UsingMeteorRequest;
import minegame159.meteorbot.json.UsingMeteorResponse;
import minegame159.meteorbot.utils.Utils;
import org.bson.Document;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.halt;

public class ApiController {
    private static String CAPE_OWNERS;
    private static String CAPES;

    private static final Map<String, Long> PLAYING = new HashMap<>();
    private static final Map<String, UUID> UUIDS = new HashMap<>();

    public static Route HANDLE_VERSION = (request, response) -> Config.VERSION;

    public static Route HANDLE_CAPE_OWNERS = (request, response) -> {
        response.type("application/json");
        return CAPE_OWNERS;
    };

    public static Route HANDLE_CAPES = (request, response) -> {
        response.type("application/json");
        return CAPES;
    };

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDSerializer())
            .create();

    public static Route HANDLE_TOGGLE_DISCORD = (request, response) -> {
        String token = request.queryParams("token");
        if (token == null || !token.equals(Config.SERVER_TOKEN)) halt(401);

        MeteorBot.PROCESS_DISCORD_EVENTS = !MeteorBot.PROCESS_DISCORD_EVENTS;
        MeteorBot.LOG.info("Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS);
        return "Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS;
    };

    public static Route HANDLE_ONLINE_PING = (request, response) -> {
        String ip = Utils.getIp(request);
        String uuid = request.queryParams("uuid");

        PLAYING.put(ip, System.currentTimeMillis());
        if (uuid != null) UUIDS.put(ip, UUID.fromString(uuid));

        return "";
    };

    public static Route HANDLE_ONLINE_LEAVE = (request, response) -> {
        String ip = Utils.getIp(request);
        PLAYING.remove(ip);
        UUIDS.remove(ip);

        return "";
    };

    public static Route HANDLE_USING_METEOR = (request, response) -> {
        UsingMeteorRequest req;
        try {
            req = GSON.fromJson(request.body(), UsingMeteorRequest.class);
        } catch (JsonIOException ignored) {
            response.status(400);
            return "";
        }

        UsingMeteorResponse res = new UsingMeteorResponse(req.uuids.size());
        for (UUID uuid : req.uuids) res.uuids.put(uuid, false);

        for (UUID uuid : UUIDS.values()) {
            for (UUID resUuid : res.uuids.keySet()) {
                if (uuid.equals(resUuid)) res.uuids.put(resUuid, true);
            }
        }

        response.type("application/json");
        return GSON.toJson(res);
    };

    public static int getOnlinePlayers() {
        return PLAYING.size();
    }

    public static void validateOnlinePlayers() {
        long time = System.currentTimeMillis();

        PLAYING.keySet().removeIf(s -> {
            if (time - PLAYING.get(s) > 6 * 60 * 1000) {
                UUIDS.remove(s);
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
