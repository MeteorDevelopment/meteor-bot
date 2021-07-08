package meteordevelopment.meteorbot.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.function.Function;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Collection<T extends ISerializable> {
    private final MongoCollection<Document> collection;
    private final Function<Document, T> factory;

    public Collection(String name, Function<Document, T> factory) {
        this.collection = Database.getCollection(name);
        this.factory = factory;
    }

    public FindIterable<Document> getAll() {
        return collection.find();
    }

    public T get(Bson filter) {
        Document document = collection.find(filter).first();
        if (document == null) return null;
        return factory.apply(document);
    }
    public T get(String id) {
        return get(eq("id", id));
    }

    public void add(T obj) {
        collection.insertOne(obj.serialize());
    }

    public void update(String id, Bson update) {
        collection.updateOne(and(eq("id", id)), update);
    }
    public void update(T obj) {
        collection.replaceOne(eq("id", obj.getId()), obj.serialize(), new ReplaceOptions().upsert(true));
    }

    public void removeAll(String id) {
        collection.deleteMany(eq("id", id));
    }
}
