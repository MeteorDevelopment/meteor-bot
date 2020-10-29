package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import org.bson.Document;

public class Cape implements ISerializable {
    public String name;
    public String url;

    public boolean selfAssignable;

    public Cape(Document document) {
        name = document.getString("id");

        selfAssignable = document.getBoolean("selfAssignable", false);
    }

    public Cape(String name, String url, boolean selfAssignable) {
        this.name = name;
        this.url = url;

        this.selfAssignable = selfAssignable;
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", name)
                .append("url", url)
                .append("selfAssignable", selfAssignable);
    }

    @Override
    public String getId() {
        return name;
    }
}
