package org.meteordev.meteorbot;

import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static final Color COLOR = new Color(145, 61, 226);

    private static final java.util.List<String> suffixes = List.of("k", "m", "b", "t");

    public static String formatLong(long value) {
        String formatted = NumberFormat.getNumberInstance(Locale.UK).format(value);
        String first = formatted, second = "", suffix = "";

        if (formatted.contains(",")) {
            int firstComma = formatted.indexOf(',');
            first = formatted.substring(0, firstComma);
            second = "." + formatted.substring(firstComma + 1, firstComma + 3);
            suffix = suffixes.get(formatted.replaceAll("[^\",\"]", "").length() - 1);
        }

        return first + second + suffix;
    }

    public static EmbedBuilder embedTitle(String title, String format, Object... args) {
        return new EmbedBuilder()
            .setColor(COLOR)
            .setTitle(title)
            .setDescription(String.format(format, args));
    }

    public static EmbedBuilder embed(String format, Object... args) {
        return embedTitle(null, format, args);
    }

    public static GetRequest apiGet(String path) {
        return Unirest.get(MeteorBot.API_BASE + path);
    }

    public static HttpRequestWithBody apiPost(String path) {
        HttpRequestWithBody req = Unirest.post(MeteorBot.API_BASE + path);
        req.header("Authorization", MeteorBot.BACKEND_TOKEN);
        return req;
    }
}
