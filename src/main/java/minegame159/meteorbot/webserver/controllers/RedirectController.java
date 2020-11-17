package minegame159.meteorbot.webserver.controllers;

import spark.Route;

public class RedirectController {
    public static Route HANDLE_DISCORD = (request, response) -> {
        response.redirect("https://discord.gg/bBGQZvd");
        return null;
    };

    public static Route HANDLE_DONATE = (request, response) -> {
        response.redirect("https://paypal.me/MineGame159");
        return null;
    };

    public static Route HANDLE_YOUTUBE = (request, response) -> {
        response.redirect("https://www.youtube.com/channel/UCqrwGisb68E2N-hA5IpyssA");
        return null;
    };

    public static Route HANDLE_GITHUB = (request, response) -> {
        response.redirect("https://www.github.com/MeteorDevelopment");
        return null;
    };
}
