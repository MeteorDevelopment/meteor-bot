package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import org.bson.Document;

public class JoinStats implements ISerializable {
    public String date;
    public int joins, leaves;

    public JoinStats(Document document) {
        date = document.getString("id");
        joins = document.getInteger("joins", 0);
        leaves = document.getInteger("leaves", 0);
    }

    public JoinStats(String date, int joins, int leaves) {
        this.date = date;
        this.joins = joins;
        this.leaves = leaves;
    }

    public int getTotalJoins() {
        return joins - leaves;
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", date)
                .append("joins", joins)
                .append("leaves", leaves);
    }

    @Override
    public String getId() {
        return date;
    }
}
