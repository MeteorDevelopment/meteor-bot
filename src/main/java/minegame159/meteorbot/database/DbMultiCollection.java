package minegame159.meteorbot.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.mongodb.client.model.Filters.eq;

public class DbMultiCollection {
    private final MongoCollection<Document> collection;
    private final Map<Class<? extends ISerializable>, Function<Document, ? extends ISerializable>> factories;

    public DbMultiCollection(String name) {
        this.collection = Db.getCollection(name);
        this.factories = new HashMap<>();
    }

    public <T extends ISerializable> void register(Class<T> klass, Function<Document, T> factory) {
        factories.put(klass, factory);
    }

    public <T extends ISerializable> T get(Class<T> klass, String id) {
        Document document = collection.find(eq("id", id)).first();
        if (document == null) return null;
        return (T) factories.get(klass).apply(document);
    }

    public <T extends ISerializable> void update(T obj) {
        collection.replaceOne(eq("id", obj.getId()), obj.serialize());
    }
}
