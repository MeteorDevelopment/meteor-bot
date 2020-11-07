package minegame159.meteorbot;

import com.mongodb.client.model.Filters;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.models.UserModel;
import minegame159.meteorbot.utils.Utils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Properties;

import static spark.Spark.*;

public class WebServer {
    private static String CAPE_OWNERS;
    private static String CAPES;

    public static void init() {
        Properties velocityProperties = new Properties();
        velocityProperties.put("resource.loader", "class");
        velocityProperties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(velocityProperties);

        port(8082);

        updateCapes();

        staticFiles.location("public");
        staticFiles.expireTime(600);

        get("/", (request, response) -> {
            VelocityContext context = new VelocityContext();

            context.put("version", Config.VERSION);
            context.put("mcVersion", Config.MC_VERSION);

            return render(context, "views/index.vm");
        });

        get("/info", (request, response) -> {
            VelocityContext context = new VelocityContext();

            context.put("version", Config.VERSION);
            context.put("changelog", Config.CHANGELOG);

            return render(context, "views/info.vm");
        });

        get("/download", (request, response) -> {
            response.header("Content-Disposition", "attachment; filename=meteor-client-0.3.6.jar");
            response.type("application/java-archive");

            try {
                OutputStream out = response.raw().getOutputStream();
                InputStream in = WebServer.class.getResourceAsStream("/meteor-client-0.3.6.mjar");

                byte[] bytes = new byte[1024];
                int read;
                while ((read = in.read(bytes)) > 0) out.write(bytes, 0, read);

                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.raw();
        });


        get("/user", (request, response) -> {
            VelocityContext context = new VelocityContext();

            String userId = request.queryParams("userId");
            User user = Db.USERS.get(userId);

            if (user != null) context.put("user", new UserModel(user));
            return render(context, "views/user.vm");
        });

        get("/api/version", (request, response) -> Config.VERSION);
        get("/api/capeowners", (request, response) -> CAPE_OWNERS);
        get("/api/capes", (request, response) -> CAPES);

        get("/api/togglediscord", (request, response) -> {
            String token = request.queryParams("token");
            if (token == null || !token.equals(Config.SERVER_TOKEN)) halt(401);

            MeteorBot.PROCESS_DISCORD_EVENTS = !MeteorBot.PROCESS_DISCORD_EVENTS;
            MeteorBot.LOG.info("Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS);
            return "Process discord events set to " + MeteorBot.PROCESS_DISCORD_EVENTS;
        });
    }

    private static String render(VelocityContext context, String template) {
        StringWriter writer = new StringWriter();
        Velocity.getTemplate(template).merge(context, writer);
        return writer.toString();
    }

    public static void updateCapes() {
        StringBuilder sb = new StringBuilder();

        // CAPE OWNERS
        int i = 0;
        for (Document document : Db.USERS.getAll().filter(Filters.ne("cape", ""))) {
            User user = new User(document);
            if (!user.hasCape()) continue;

            if (!user.mcAccounts.isEmpty()) {
                String cape = user.cape;
                if (cape.equals("custom")) cape = user.id;

                for (String uuid : user.mcAccounts) {
                    if (i > 0) sb.append("\n");
                    sb.append(Utils.newUUID(uuid)).append(" ").append(cape);
                    i++;
                }
            }
        }

        CAPE_OWNERS = sb.toString();
        sb.setLength(0);

        // CAPES
        i = 0;
        for (Document document : Db.CAPES.getAll()) {
            if (i > 0) sb.append("\n");
            sb.append(document.getString("id")).append(" ").append(document.getString("url"));
            i++;
        }

        CAPES = sb.toString();
    }

    public static void close() {
        stop();
        awaitStop();
    }
}
