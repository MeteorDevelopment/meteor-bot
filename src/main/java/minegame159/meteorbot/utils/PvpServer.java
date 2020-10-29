package minegame159.meteorbot.utils;

import kong.unirest.Unirest;
import minegame159.meteorbot.database.documents.User;

public class PvpServer {
    private static final String TOKEN = "M2LoLF4iZSAJt25kF84l";

    public static void giveDonatorToAll(User user) {
        for (String uuid : user.mcAccounts) giveDonator(uuid);
    }

    public static void giveDonator(String uuid) {
        Unirest.post("http://mc.meteorclient.com:8115/adddonator")
                .queryString("token", TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }

    public static void removeDonatorFromAll(User user) {
        for (String uuid : user.mcAccounts) removeDonator(uuid);
    }

    public static void removeDonator(String uuid) {
        Unirest.post("http://mc.meteorclient.com:8115/removedonator")
                .queryString("token", TOKEN)
                .queryString("uuid", Utils.newUUID(uuid))
                .asEmpty();
    }
}
