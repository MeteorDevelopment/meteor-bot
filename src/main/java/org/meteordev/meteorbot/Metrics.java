package org.meteordev.meteorbot;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Metrics extends ListenerAdapter {
    private Guild guild;
    private HttpServer server;

    @Override
    public void onReady(ReadyEvent event) {
        JDA bot = event.getJDA();

        guild = bot.getGuildById(Env.GUILD_ID.value);
        if (guild == null) {
            MeteorBot.LOG.error("Couldn't find the specified server.");
            System.exit(1);
        }

        try {
            server = HttpServer.create(new InetSocketAddress(9400), 0);
            server.createContext("/metrics", this::onRequest);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MeteorBot.LOG.info("Providing metrics on :9400/metrics");
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        server.stop(1000);
    }

    private void onRequest(HttpExchange exchange) {
        byte[] response = String.format("""
            # HELP meteor_discord_users_total Total number of Discord users in our server
            # TYPE meteor_discord_users_total gauge
            meteor_discord_users_total %d
            """, guild.getMemberCount()).getBytes(StandardCharsets.UTF_8);

        try {
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OutputStream out = exchange.getResponseBody();
        copy(response, out);
    }

    @SuppressWarnings("ThrowFromFinallyBlock")
    private static void copy(byte[] in, OutputStream out) {
        try {
            out.write(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
