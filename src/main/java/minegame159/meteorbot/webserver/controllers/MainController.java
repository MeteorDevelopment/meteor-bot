package minegame159.meteorbot.webserver.controllers;

import minegame159.meteorbot.Config;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Stats;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.webserver.WebServer;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.apache.velocity.VelocityContext;
import spark.Route;

import java.io.InputStream;
import java.io.OutputStream;

public class MainController {
    private static int DOWNLOADS;

    public static void init() {
        DOWNLOADS = Db.GLOBAL.get(Stats.class, Stats.ID).downloads;
    }

    public static Route SERVER_INDEX = (request, response) -> {
        VelocityContext context = new VelocityContext();

        context.put("version", Config.VERSION);
        context.put("mcVersion", Config.MC_VERSION);
        context.put("downloads", DOWNLOADS);

        return WebServer.render(context, "views/index.html");
    };

    public static Route SERVER_INFO = (request, response) -> {
        VelocityContext context = new VelocityContext();

        context.put("version", Config.VERSION);
        context.put("mcVersion", Config.MC_VERSION);
        context.put("changelog", Config.CHANGELOG);

        return WebServer.render(context, "views/info.html");
    };

    public static Route HANDLE_DOWNLOAD = (request, response) -> {
        Stats stats = Db.GLOBAL.get(Stats.class, Stats.ID);
        stats.downloads++;
        DOWNLOADS++;
        Db.GLOBAL.update(stats);

        VoiceChannel channel = Utils.findVoiceChannel(MeteorBot.JDA.getGuildById("689197705683140636"), "Downloads: ");
        if (channel != null) channel.getManager().setName("Downloads: " + DOWNLOADS).queue();

        response.header("Content-Disposition", "attachment; filename=meteor-client-" + Config.VERSION + ".jar");
        response.type("application/java-archive");

        OutputStream out = response.raw().getOutputStream();
        InputStream in = WebServer.class.getResourceAsStream("/meteor-client-" + Config.VERSION + ".mjar");
        Utils.copyStream(in, out);

        return response.raw();
    };
}
