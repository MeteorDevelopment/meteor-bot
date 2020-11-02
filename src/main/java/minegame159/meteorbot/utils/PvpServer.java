package minegame159.meteorbot.utils;

import kong.unirest.Unirest;

public class PvpServer {
    private static final String TOKEN = "M2LoLF4iZSAJt25kF84l";

    public static void giveDonator(String uuid) {
        Unirest.post("http://pvp.meteorclient.com:8115/adddonator")
                .queryString("token", TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }

    public static void removeDonator(String uuid) {
        Unirest.post("http://pvp.meteorclient.com:8115/removedonator")
                .queryString("token", TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }

    public static void toggleDonator(String uuid) {
        Unirest.post("http://pvp.meteorclient.com:8115/toggledonator")
                .queryString("token", TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }
}
