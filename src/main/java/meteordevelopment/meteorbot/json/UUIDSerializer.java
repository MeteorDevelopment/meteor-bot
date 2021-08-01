package meteordevelopment.meteorbot.json;

import com.google.gson.*;
import meteordevelopment.meteorbot.utils.Utils;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDSerializer implements JsonSerializer<UUID>, JsonDeserializer<UUID> {
    @Override
    public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Utils.getUuid(json.getAsString());
    }
}
