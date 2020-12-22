package minegame159.meteorbot.webserver.controllers;

import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.webserver.Accounts;
import minegame159.meteorbot.webserver.Attribs;
import spark.Route;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;

public class CapesController {
    public static Route SERVE_CAPES = (request, response) -> {
        File file = new File("capes", request.params(":name"));

        if (file.exists() && file.isFile() && file.getName().endsWith(".png")) {
            response.type("image/png");

            InputStream in = new FileInputStream(file);
            OutputStream out = response.raw().getOutputStream();
            Utils.copyStream(in, out);
        }

        return null;
    };

    public static Route HANDLE_UPLOAD_CUSTOM = LoginController.ensureLoggedIn((request, response) -> {
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        Part part = request.raw().getPart("file");
        if (part != null) {
            try (InputStream in = part.getInputStream()) {
                Account account = Accounts.get(request);
                String name = "account_" + account.id;

                File file = new File("capes", name + ".png");
                file.getParentFile().mkdirs();
                OutputStream out = new FileOutputStream(file);
                Utils.copyStream(in, out);

                boolean ok = true;
                BufferedImage image = ImageIO.read(file);

                if (image.getHeight() * 2 != image.getWidth()) {
                    Attribs.CUSTOM_CAPE_ERROR.set(request, "Wrong size. Width of the image must be double the height.");
                    ok = false;
                } else if (image.getWidth() > 1024 || image.getHeight() > 512) {
                    Attribs.CUSTOM_CAPE_ERROR.set(request, "Maximum image size is 1024x512.");
                    ok = false;
                }

                if (ok) {
                    Cape cape = Db.CAPES.get(name);
                    if (cape == null) {
                        cape = new Cape(name, "http://meteorclient.com:8082/capes/" + name + ".png");
                        Db.CAPES.add(cape);

                        ApiController.updateCapes();
                    }
                } else {
                    file.delete();
                }
            }
        }

        response.redirect("/account");
        return null;
    }, false);
}
