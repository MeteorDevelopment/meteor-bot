package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import org.bson.Document;

public class Stats implements ISerializable {
    public static final String ID = "Stats";

    public int downloads;

    public Stats(Document document) {
        downloads = document.getInteger("downloads", 0);
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", ID)
                .append("downloads", downloads);
    }

    @Override
    public String getId() {
        return ID;
    }
}
