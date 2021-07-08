package meteordevelopment.meteorbot.database.documents;

import meteordevelopment.meteorbot.database.ISerializable;
import org.bson.Document;

public class DbDailyStats implements ISerializable {
    public String date;
    public int joins, leaves;
    public int downloads;

    public DbDailyStats(Document document) {
        date = document.getString("id");
        joins = document.getInteger("joins", 0);
        leaves = document.getInteger("leaves", 0);
        downloads = document.getInteger("downloads", 0);
    }

    public DbDailyStats(String date, int joins, int leaves) {
        this.date = date;
        this.joins = joins;
        this.leaves = leaves;
        this.downloads = 0;
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
            .append("downloads", downloads);
    }

    @Override
    public String getId() {
        return date;
    }
}
