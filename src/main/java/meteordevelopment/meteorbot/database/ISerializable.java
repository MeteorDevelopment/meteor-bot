package meteordevelopment.meteorbot.database;

import org.bson.Document;

public interface ISerializable {
    Document serialize();

    String getId();
}
