package minegame159.meteorbot.utils;

import kong.unirest.Unirest;
import minegame159.meteorbot.Config;

public class PvpServer {
    public static void giveDonator(String uuid) {
        Unirest.post("http://pvp.meteorclient.com:8115/adddonator")
                .queryString("token", Config.PVP_SERVER_TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }

    public static void removeDonator(String uuid) {
        Unirest.post("http://pvp.meteorclient.com:8115/removedonator")
                .queryString("token", Config.PVP_SERVER_TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }

    public static void toggleDonator(String uuid) {
        Unirest.post("http://pvp.meteorclient.com:8115/toggledonator")
                .queryString("token", Config.PVP_SERVER_TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }
}
