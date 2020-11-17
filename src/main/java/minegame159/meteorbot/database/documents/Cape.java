package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.database.ISerializable;
import org.bson.Document;

public class Cape implements ISerializable {
    public String name;
    public String url;

    private String title;
    private boolean current;

    public Cape(Document document) {
        name = document.getString("id");
        url = document.getString("url");
    }

    public Cape(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Cape set(Account account, String title) {
        this.title = title;
        this.current = account.cape.equals(name);

        if (account.cape.equals("custom") && name.equals("account_" + account.id)) this.current = true;

        return this;
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", name)
                .append("url", url);
    }

    @Override
    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        if (title == null) return name.substring(0, 1).toUpperCase() + name.substring(1);
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCurrent() {
        return current;
    }
}
