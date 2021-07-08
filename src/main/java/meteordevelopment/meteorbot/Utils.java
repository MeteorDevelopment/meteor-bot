package meteordevelopment.meteorbot;

import meteordevelopment.meteorbot.database.Database;
import meteordevelopment.meteorbot.database.documents.DbDailyStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.mongodb.client.model.Updates.inc;

public class Utils {
    public static final Color EMBED_COLOR = new Color(170, 85, 220);

    public static EmbedBuilder embedTitle(String title, String format, Object... args) {
        return new EmbedBuilder()
            .setColor(EMBED_COLOR)
            .setTitle(title)
            .setDescription(String.format(format, args));
    }

    public static EmbedBuilder embed(String format, Object... args) {
        return embedTitle(null, format, args);
    }

    public static void updateMemberCount(Guild guild, boolean joined) {
        VoiceChannel channel = findVoiceChannel(guild, "Member Count: ");
        if (channel != null) channel.getManager().setName("Member Count: " + guild.getMemberCount()).queue();

        String date = getDateString();
        DbDailyStats dailyStats = Database.DAILY_STATS.get(date);

        if (dailyStats != null) {
            if (joined) {
                Database.DAILY_STATS.update(date, inc("joins", 1));
            } else {
                Database.DAILY_STATS.update(date, inc("leaves", 1));
            }
        } else {
            Database.DAILY_STATS.add(new DbDailyStats(date, joined ? 1 : 0, joined ? 0 : 1));
        }
    }

    public static String getDateString() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(new Date());

        return String.format("%02d-%02d-%d", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    public static VoiceChannel findVoiceChannel(Guild guild, String startsWith) {
        for (VoiceChannel channel : guild.getVoiceChannelCache()) {
            if (channel.getName().startsWith(startsWith)) return channel;
        }

        return null;
    }

    public static boolean isDev(Member member) {
        return member.getRoles().contains(MeteorBot.DEV_ROLE);
    }
    public static boolean isMod(Member member) {
        return member.getRoles().contains(MeteorBot.MOD_ROLE);
    }
    public static boolean isHelper(Member member) {
        return member.getRoles().contains(MeteorBot.HELPER_ROLE);
    }
    public static boolean isDonator(Member member) {
        return member.getRoles().contains(MeteorBot.DONATOR_ROLE);
    }
    public static boolean isStaff(Member member) {
        return isHelper(member) || isMod(member) || isDev(member);
    }
}
