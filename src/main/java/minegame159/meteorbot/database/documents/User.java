package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class User implements ISerializable {
    public String id;

    public int niggerCount, niggaCount, nwords;

    public List<String> mcAccounts;
    public int maxMcAccounts;

    public boolean donator;
    public String cape;
    public boolean hasCustomCape;

    public User(Document document) {
        id = document.getString("id");

        niggerCount = document.getInteger("niggerCount", 0);
        niggaCount = document.getInteger("niggaCount", 0);
        nwords = document.getInteger("nwords", 0);

        mcAccounts = document.getList("mcAccounts", String.class, new ArrayList<>(0));
        maxMcAccounts = document.getInteger("maxMcAccounts", 1);

        donator = document.getBoolean("donator", false);
        cape = document.getString("cape");
        hasCustomCape = document.getBoolean("hasCustomCape", false);
    }

    public User(ISnowflake id) {
        this.id = id.getId();

        this.niggerCount = 0;
        this.niggaCount = 0;
        this.nwords = 0;

        this.mcAccounts = new ArrayList<>(0);
        this.maxMcAccounts = 1;

        this.donator = false;
        this.cape = "";
        this.hasCustomCape = false;
    }

    public void updateNwords(int niggerCount, int niggaCount) {
        this.niggerCount += niggerCount;
        this.niggaCount += niggaCount;
        this.nwords += niggerCount + niggaCount;
    }

    public boolean hasCape() {
        return !cape.isEmpty();
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", id)
                .append("niggerCount", niggerCount)
                .append("niggaCount", niggaCount)
                .append("nwords", nwords)
                .append("mcAccounts", mcAccounts)
                .append("maxMcAccounts", maxMcAccounts)
                .append("donator", donator)
                .append("cape", cape)
                .append("hasCustomCape", hasCustomCape);
    }

    @Override
    public String getId() {
        return id;
    }
}
