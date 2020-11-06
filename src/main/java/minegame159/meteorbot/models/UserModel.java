package minegame159.meteorbot.models;

import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private final String name;

    private final int niggerCount;
    private final int niggaCount;

    private final List<String> mcAccounts;

    private final String donator;
    private final String capeAccess;
    private final String capeName;
    private final String capeUrl;
    private final String hasCustomCape;

    public UserModel(User user) {
        name = MeteorBot.JDA.retrieveUserById(user.id).complete().getName();

        niggerCount = user.niggerCount;
        niggaCount = user.niggaCount;

        mcAccounts = new ArrayList<>();
        for (String uuid : user.mcAccounts) {
            mcAccounts.add(Utils.getMcUsername(uuid));
        }

        donator = user.donator ? "yes" : "no";
        capeAccess = user.capeAccess ? "yes" : "no";
        capeName = user.cape;
        Cape cape = Db.CAPES.get(user.cape.equals("custom") ? user.id : user.cape);
        capeUrl = cape == null ? "" : cape.url;
        hasCustomCape = user.hasCustomCape ? "yes" : "no";
    }

    public String getName() {
        return name;
    }

    public int getNiggerCount() {
        return niggerCount;
    }

    public int getNiggaCount() {
        return niggaCount;
    }

    public List<String> getMcAccounts() {
        return mcAccounts;
    }

    public String getDonator() {
        return donator;
    }

    public String getCapeAccess() {
        return capeAccess;
    }

    public String getCapeName() {
        return capeName;
    }

    public String getCapeUrl() {
        return capeUrl;
    }

    public String getHasCustomCape() {
        return hasCustomCape;
    }
}
