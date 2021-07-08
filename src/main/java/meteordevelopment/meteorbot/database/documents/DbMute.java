package meteordevelopment.meteorbot.database.documents;

import meteordevelopment.meteorbot.database.ISerializable;
import org.bson.Document;

public class DbMute implements ISerializable {
    public String user;
    public long start;
    public int duration;
    public String reason;
    public int count;

    public DbMute(String user, long start, int duration, String reason) {
        this.user = user;
        this.start = start;
        this.duration = duration;
        this.reason = reason;
        this.count = 1;
    }

    public DbMute(Document document) {
        this.user = document.getString("id");
        this.start = document.getLong("start");
        this.duration = document.getInteger("duration");
        this.reason = document.getString("reason");
        this.count = document.getInteger("count");
    }

    @Override
    public Document serialize() {
        return new Document()
            .append("id", user)
            .append("start", start)
            .append("duration", duration)
            .append("reason", reason)
            .append("count", count);
    }

    @Override
    public String getId() {
        return user;
    }
}
