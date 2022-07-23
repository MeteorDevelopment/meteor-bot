package meteordevelopment.meteorbot;

import kong.unirest.Unirest;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Uptime {
    public static void init() {
        if (Config.UPTIME_URL == null) {
            MeteorBot.LOG.warn("Uptime URL not configured, uptime requests will not be made");
            return;
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String url = Config.UPTIME_URL;
            if (url.endsWith("ping=")) url += MeteorBot.JDA.getGatewayPing();

            Unirest.get(url).asEmpty();
        }, 0, 60, TimeUnit.SECONDS);
    }
}
