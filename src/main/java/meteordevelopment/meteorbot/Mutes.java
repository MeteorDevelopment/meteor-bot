package meteordevelopment.meteorbot;

import meteordevelopment.meteorbot.database.Database;
import meteordevelopment.meteorbot.database.documents.DbMute;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;

public class Mutes {
    private static final List<DbMute> MUTES = new ArrayList<>();

    public static void init() {
        for (Document document : Database.MUTES.getAll()) {
            mute(new DbMute(document));
        }

        // Checking if mutes are done
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            synchronized (MUTES) {
                for (DbMute mute : MUTES) {
                    if (Instant.now().getEpochSecond() - mute.start > mute.duration) unmute(mute.user);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);


        // Updating channel overrides
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(Mutes::updateChannels, 0, 30, TimeUnit.MINUTES);
    }

    private static void updateChannels() {
        for (GuildChannel channel : MeteorBot.GUILD.getChannels()) {
            if (!(channel instanceof TextChannel)) continue;

            boolean hadPermOverride = false;

            for (PermissionOverride rolePermissionOverride : channel.getRolePermissionOverrides()) {
                if (rolePermissionOverride.getRole() == MeteorBot.MUTED_ROLE) {
                    channel.putPermissionOverride(MeteorBot.MUTED_ROLE).deny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).queue();
                    hadPermOverride = true;
                    break;
                }
            }

            if (!hadPermOverride) {
                channel.createPermissionOverride(MeteorBot.MUTED_ROLE).deny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).queue();
            }
        }
    }

    private static void mute(DbMute mute) {
        mute(mute.user, mute.duration, mute.reason);
    }

    public static void mute(String user, int duration, String reason) {
        MUTES.removeIf(mute -> mute.user.equals(user));
        MUTES.add(new DbMute(user, Instant.now().getEpochSecond(), duration, reason));

        DbMute mute = Database.MUTES.get(user);
        if (mute != null) {
            Database.MUTES.update(user, set("start", Instant.now().getEpochSecond()));
            Database.MUTES.update(user, set("duration", duration));
            Database.MUTES.update(user, set("reason", reason));
            Database.MUTES.update(user, inc("count", 1));
        } else {
            Database.MUTES.add(new DbMute(user, Instant.now().getEpochSecond(), duration, reason));
        }

        updateChannels();
        MeteorBot.GUILD.addRoleToMember(user, MeteorBot.MUTED_ROLE).queue();
    }

    public static void unmute(String user) {
        MUTES.removeIf(mute -> mute.user.equals(user));

        Database.MUTES.update(user, set("duration", 0));
        Database.MUTES.update(user, set("reason", ""));

        updateChannels();
        MeteorBot.GUILD.removeRoleFromMember(user, MeteorBot.MUTED_ROLE).queue();
    }

    public static boolean isMuted(String user) {
        for (DbMute mute : MUTES) {
            if (mute.user.equals(user)) return true;
        }

        return false;
    }
}
