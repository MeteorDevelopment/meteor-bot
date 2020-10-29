package minegame159.meteorbot.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.function.Function;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DbCollection<T extends ISerializable> {
    private final MongoCollection<Document> collection;
    private final Function<Document, T> factory;

    public DbCollection(String name, Function<Document, T> factory) {
        this.collection = Db.getCollection(name);
        this.factory = factory;
    }

    public void add(T obj) {
        collection.insertOne(obj.serialize());
    }

    public T get(String id) {
        Document document = collection.find(eq("id", id)).first();
        if (document == null) return null;
        return factory.apply(document);
    }
    public T get(ISnowflake id) {
        return get(id.getId());
    }

    public FindIterable<Document> getAll() {
        return collection.find();
    }

    public void update(String id, Bson update) {
        collection.updateOne(and(eq("id", id)), update);
    }
    public void update(ISnowflake id, Bson update) {
        update(id.getId(), update);
    }

    public void update(T obj) {
        collection.replaceOne(eq("id", obj.getId()), obj.serialize());
    }

    public void delete(String id) {
        collection.deleteOne(eq("id", id));
    }
}
