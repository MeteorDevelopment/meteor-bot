package org.meteordev.meteorbot;

import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern TIME_PATTERN = Pattern.compile("^(\\d+)(ms|s|m|h|d|w)$");
    public static final Color COLOR = new Color(145, 61, 226);

    private static final List<String> suffixes = List.of("k", "m", "b", "t");

    private static final String API_BASE = System.getenv("API_BASE");
    private static final String BACKEND_TOKEN = System.getenv("BACKEND_TOKEN");

    public static long parseAmount(String parse) {
        Matcher matcher = TIME_PATTERN.matcher(parse);
        if (!matcher.matches() || matcher.groupCount() != 2) return -1;

        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    public static ChronoUnit parseUnit(String parse) {
        Matcher matcher = TIME_PATTERN.matcher(parse);
        if (!matcher.matches() || matcher.groupCount() != 2) return null;

        return unitFromString(matcher.group(2));
    }

    public static ChronoUnit unitFromString(String string) {
        for (ChronoUnit value : ChronoUnit.values()) {
            String stringValue = unitToString(value);
            if (stringValue == null) continue;

            if (stringValue.equalsIgnoreCase(string)) {
                return value;
            }
        }

        return null;
    }

    public static String unitToString(ChronoUnit unit) {
        return switch (unit) {
            case SECONDS -> "s";
            case MINUTES -> "m";
            case HOURS -> "h";
            case DAYS -> "d";
            case WEEKS -> "w";
            default -> null;
        };
    }


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
        return new EmbedBuilder().setColor(COLOR).setTitle(title).setDescription(String.format(format, args));
    }

    public static EmbedBuilder embed(String format, Object... args) {
        return embedTitle(null, format, args);
    }

    public static GetRequest apiGet(String path) {
        return Unirest.get(API_BASE + path);
    }

    public static HttpRequestWithBody apiPost(String path) {
        HttpRequestWithBody req = Unirest.post(API_BASE + path);
        req.header("Authorization", BACKEND_TOKEN);
        return req;
    }
}
