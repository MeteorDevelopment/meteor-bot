package minegame159.meteorbot;

import minegame159.meteorbot.commands.Commands;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.utils.Audio;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.regex.Pattern;

public class MeteorBot extends ListenerAdapter {
    public static final Logger LOG = JDALogger.getLog("MeteorBot");

    private static final Pattern NIGGER_PATTERN = Pattern.compile("nigger", Pattern.CASE_INSENSITIVE);
    private static final Pattern NIGGA_PATTERN = Pattern.compile("nigga", Pattern.CASE_INSENSITIVE);

    public static boolean PROCESS_DISCORD_EVENTS = true;

    public static void main(String[] args) {
        try {
            Config.init();

            JDABuilder.createDefault(Config.DISCORD_TOKEN)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new MeteorBot())
                    .build();

        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing("Meteor on Crack!"));

        Db.init();
        Audio.init();
        Commands.init();
        WebServer.init();

        LOG.info("Meteor Bot started");
    }

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        WebServer.close();
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (!PROCESS_DISCORD_EVENTS) return;
        if (event.getAuthor().isBot() || !event.isFromType(ChannelType.TEXT)) return;
        String str = event.getMessage().getContentRaw();

        // Commands
        Commands.onMessage(event);

        // Count nwords
        int niggerCount = Utils.count(str, NIGGER_PATTERN);
        int niggaCount = Utils.count(str, NIGGA_PATTERN);

        if (niggerCount > 2) niggerCount = 0;
        if (niggaCount > 2) niggaCount = 0;

        if (niggerCount > 0 || niggaCount > 0) {
            User user = Db.USERS.get(event.getAuthor());

            if (user != null) {
                user.updateNwords(niggerCount, niggaCount);
                Db.USERS.update(user);
            } else {
                user = new User(event.getAuthor());
                user.updateNwords(niggerCount, niggaCount);
                Db.USERS.add(user);
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        if (!PROCESS_DISCORD_EVENTS) return;
        Utils.updateMemberCount(event.getGuild(), true);
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        if (!PROCESS_DISCORD_EVENTS) return;
        Utils.updateMemberCount(event.getGuild(), false);
    }
}
