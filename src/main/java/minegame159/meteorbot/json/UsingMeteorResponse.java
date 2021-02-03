package minegame159.meteorbot.json;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsingMeteorResponse {
    public Map<UUID, Boolean> uuids;

    public UsingMeteorResponse(int size) {
        uuids = new HashMap<>(size);
    }
}
