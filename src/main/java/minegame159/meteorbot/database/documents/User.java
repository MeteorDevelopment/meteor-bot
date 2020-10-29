package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.bson.Document;

public class User implements ISerializable {
    public String id;
    public int niggerCount, niggaCount, nwords;

    public User(Document document) {
        id = document.getString("id");
        niggerCount = document.getInteger("niggerCount", 0);
        niggaCount = document.getInteger("niggaCount", 0);
        nwords = document.getInteger("nwords", 0);
    }

    public User(ISnowflake id, int niggerCount, int niggaCount) {
        this.id = id.getId();
        this.niggerCount = niggerCount;
        this.niggaCount = niggaCount;
        this.nwords = niggerCount + niggaCount;
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", id)
                .append("niggerCount", niggerCount)
                .append("niggaCount", niggaCount)
                .append("nwords", nwords);
    }
}
