package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import org.bson.Document;

public class Stats implements ISerializable {
    public static final String ID = "Stats";

    public int downloads;
    public int totalAccounts;

    public long supportMessage;

    public Stats(Document document) {
        downloads = document.getInteger("downloads", 0);
        totalAccounts = document.getInteger("totalAccounts", 0);
        supportMessage = document.get("supportMessage", 0L);
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", ID)
                .append("downloads", downloads)
                .append("totalAccounts", totalAccounts)
                .append("supportMessage", supportMessage);
    }

    @Override
    public String getId() {
        return ID;
    }
}
