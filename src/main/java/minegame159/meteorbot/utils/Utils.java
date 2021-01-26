package minegame159.meteorbot.utils;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.DailyStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import spark.Request;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Updates.inc;

public class Utils {
    private static final Color EMBED_COLOR = new Color(204, 0, 0);

    public static void copyStream(InputStream in, OutputStream out) {
        try {
            byte[] bytes = new byte[1024];
            int read;
            while ((read = in.read(bytes)) > 0) out.write(bytes, 0, read);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateToken(boolean small) {
        UUID id = UUID.randomUUID();
        long hi = id.getMostSignificantBits();
        long lo = id.getLeastSignificantBits();

        byte[] bytes;
        if (small) bytes = ByteBuffer.allocate(2).putShort((short) hi).array();
        else bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();

        BigInteger big = new BigInteger(bytes);
        return big.toString().replace('-','1');
    }
    public static String generateToken() {
        return generateToken(false);
    }

    public static EmbedBuilder embedTitle(String title, String format, Object... args) {
        return new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle(title)
                .setDescription(String.format(format, args));
    }

    public static EmbedBuilder embed(String format, Object... args) {
        return embedTitle(null, format, args);
    }

    public static int count(String str, Pattern pattern) {
        Matcher matcher = pattern.matcher(str);
        int count = 0;

        while (matcher.find()) count++;

        return count;
    }

    public static void updateMemberCount(Guild guild, boolean joined) {
        VoiceChannel channel = findVoiceChannel(guild, "Member Count: ");
        if (channel != null) channel.getManager().setName("Member Count: " + guild.getMemberCount()).queue();

        String date = getDateString();
        DailyStats dailyStats = Db.DAILY_STATS.get(date);

        if (dailyStats != null) {
            if (joined) {
                Db.DAILY_STATS.update(date, inc("joins", 1));
            } else {
                Db.DAILY_STATS.update(date, inc("leaves", 1));
            }
        } else {
            Db.DAILY_STATS.add(new DailyStats(date, joined ? 1 : 0, joined ? 0 : 1));
        }
    }

    public static String getDateString() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(new Date());

        return String.format("%d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1);
    }

    public static VoiceChannel findVoiceChannel(Guild guild, String startsWith) {
        for (VoiceChannel channel : guild.getVoiceChannelCache()) {
            if (channel.getName().startsWith(startsWith)) return channel;
        }

        return null;
    }

    public static String getMcUuid(String username) {
        HttpResponse<JsonNode> response = Unirest
                .post("https://api.mojang.com/profiles/minecraft")
                .header("Content-Type", "application/json")
                .body(new JSONArray().put(username))
                .asJson();

        try {
            return response.getBody().getArray().getJSONObject(0).getString("id");
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String getMcUsername(String uuid) {
        HttpResponse<JsonNode> response = Unirest
                .get("https://api.mojang.com/user/profiles/" + uuid + "/names")
                .asJson();

        try {
            String username = null;
            long time = 0;

            JSONArray array = response.getBody().getArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                if (!o.has("changedToAt")) o.put("changedToAt", 1);

                if (o.getLong("changedToAt") > time) {
                    username = o.getString("name");
                    time = o.getLong("changedToAt");
                }
            }

            return username;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String boolToString(boolean bool) {
        return bool ? "yes" : "no";
    }

    public static boolean stringToBool(String str) {
        return str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("true");
    }

    public static boolean isMod(Member member) {
        return member.hasPermission(Permission.MANAGE_ROLES);
    }

    public static boolean onlyMod(MessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage(Utils.embed("Only for mods ;)").build()).queue();
            return false;
        }

        return true;
    }

    public static Member onlyModWithPing(MessageReceivedEvent event, String[] split) {
        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage(Utils.embed("Only for mods ;)").build()).queue();
            return null;
        }

        Member member = null;

        if (split.length > 1 && split[1].startsWith("<@!") && split[1].endsWith(">")) {
            String id = split[1].substring(3, split[1].length() - 1);
            member = event.getGuild().retrieveMemberById(id).complete();
        }

        if (member == null) {
            event.getChannel().sendMessage(Utils.embed("You need to ping the person.").build()).queue();
            return null;
        }

        return member;
    }

    public static UUID newUUID(String uuid) {
        return UUID.fromString(uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

    public static String getIp(Request req) {
        String ip = req.headers("X-Forwarded-For");
        if (ip != null) return ip;
        return req.ip();
    }
}
