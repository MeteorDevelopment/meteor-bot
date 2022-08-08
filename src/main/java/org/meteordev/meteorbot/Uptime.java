package org.meteordev.meteorbot;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Uptime extends ListenerAdapter {
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        if (MeteorBot.UPTIME_URL == null) {
            MeteorBot.LOG.warn("Uptime URL not configured, uptime requests will not be made");
            return;
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String url = MeteorBot.UPTIME_URL;
            if (url.endsWith("ping=")) url += MeteorBot.JDA.getGatewayPing();

            Unirest.get(url).asEmpty();
        }, 0, 60, TimeUnit.SECONDS);
    }
}
