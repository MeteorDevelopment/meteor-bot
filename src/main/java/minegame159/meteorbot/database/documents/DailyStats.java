package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import org.bson.Document;

public class DailyStats implements ISerializable {
    public String date;
    public int joins, leaves;
    public int downloads;
    public int websiteVisits;

    public DailyStats(Document document) {
        date = document.getString("id");
        joins = document.getInteger("joins", 0);
        leaves = document.getInteger("leaves", 0);
        downloads = document.getInteger("downloads", 0);
        websiteVisits = document.getInteger("websiteVisits", 0);
    }

    public DailyStats(String date, int joins, int leaves) {
        this.date = date;
        this.joins = joins;
        this.leaves = leaves;
        this.downloads = 0;
        this.websiteVisits = 0;
    }

    public int getTotalJoins() {
        return joins - leaves;
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", date)
                .append("joins", joins)
                .append("leaves", leaves)
                .append("downloads", downloads)
                .append("websiteVisits", websiteVisits);
    }

    @Override
    public String getId() {
        return date;
    }
}
