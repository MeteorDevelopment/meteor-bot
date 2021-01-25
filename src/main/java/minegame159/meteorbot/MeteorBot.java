package minegame159.meteorbot;

import minegame159.meteorbot.commands.Commands;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.tickets.Tickets;
import minegame159.meteorbot.utils.Audio;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.webserver.WebServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class MeteorBot extends ListenerAdapter {
    public static final Logger LOG = JDALogger.getLog("MeteorBot");

    public static JDA JDA;
    public static Guild GUILD;
    public static Role MOD_ROLE, HELPER_ROLE;
    public static User MINEGAME, SQUID, SEASNAIL;


    public static boolean PROCESS_DISCORD_EVENTS = true;

    public static void main(String[] args) {
        try {
            Config.init();
            Db.init();
            Audio.init();
            Commands.init();
            WebServer.init();

            JDABuilder.createDefault(Config.DISCORD_TOKEN)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .enableCache(CacheFlag.EMOTE)
                    .addEventListeners(new MeteorBot())
                    .build();

        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        JDA = event.getJDA();
        event.getJDA().getPresence().setActivity(Activity.playing("Meteor on Crack!"));

        GUILD = JDA.getGuildById(689197705683140636L);
        MINEGAME = GUILD.retrieveMemberById(205708530408357898L).complete().getUser();
        SQUID = GUILD.retrieveMemberById(322777907078627328L).complete().getUser();
        SEASNAIL = GUILD.retrieveMemberById(736954747122352208L).complete().getUser();
        MOD_ROLE = GUILD.getRoleById(689197893340758022L);
        HELPER_ROLE = GUILD.getRoleById(799392357157830657L);

        Tickets.init();

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

        Commands.onMessage(event);
        Tickets.onMessage(event);
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (!PROCESS_DISCORD_EVENTS) return;
        Tickets.onReactionAdd(event);
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
