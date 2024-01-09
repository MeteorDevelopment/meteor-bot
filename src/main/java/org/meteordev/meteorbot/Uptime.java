package org.meteordev.meteorbot;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Uptime extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if (Env.UPTIME_URL.value == null) {
            MeteorBot.LOG.warn("Uptime URL not configured, uptime requests will not be made");
            return;
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String url = Env.UPTIME_URL.value;
            if (url.endsWith("ping=")) url += event.getJDA().getGatewayPing();

            Unirest.get(url).asEmpty();
        }, 0, 60, TimeUnit.SECONDS);
    }
}
