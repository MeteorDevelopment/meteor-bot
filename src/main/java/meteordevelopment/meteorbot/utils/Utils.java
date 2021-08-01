package meteordevelopment.meteorbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {
    private static final Color EMBED_COLOR = new Color(204, 0, 0);
    private static final Pattern pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    public static UUID getUuid(String string) throws IllegalArgumentException {
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException ignored) {
            try {
                return UUID.fromString(pattern.matcher(string).replaceAll("$1-$2-$3-$4-$5"));
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage(), e.getCause());
            }
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

    public static EmbedBuilder embedTitle(String title, String format, Object... args) {
        return new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle(title)
                .setDescription(String.format(format, args));
    }

    public static EmbedBuilder embed(String format, Object... args) {
        return embedTitle(null, format, args);
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

    public static boolean onlyMod(MessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage(Utils.embed("Only for mods ;)").build()).queue();
            return false;
        }

        return true;
    }
}
