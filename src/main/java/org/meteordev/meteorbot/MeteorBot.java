package org.meteordev.meteorbot;

import org.meteordev.meteorbot.command.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class MeteorBot extends ListenerAdapter {
    private static final String[] HELLOS = { "Hi", "Hello", "Helo", "Hewo", "Hewwo", "Howdy", "Bonjour" };
    public static final Logger LOG = JDALogger.getLog("MeteorBot");

    public static JDA JDA;
    public static Guild GUILD;
    public static long GUILD_ID = 689197705683140636L;
    public static TextChannel MOD_LOG;
    public static long MOD_LOG_ID = 847134083431792701L;
    public static Emoji UWUCAT;
    public static long UWUCAT_ID = 806473609526509578L;

    public static void main(String[] args) {
        try {
            Config.init();

            JDABuilder.createDefault(Config.DISCORD_TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .enableCache(CacheFlag.EMOJI)
                .addEventListeners(new MeteorBot(), new Commands(), new Uptime(), new InfoChannels())
                .build();
        }
        catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        JDA = event.getJDA();
        JDA.getPresence().setActivity(Activity.playing("Meteor Client"));
        GUILD = JDA.getGuildById(GUILD_ID);
        MOD_LOG = GUILD.getTextChannelById(MOD_LOG_ID);
        UWUCAT = GUILD.retrieveEmojiById(UWUCAT_ID).complete();

        LOG.info("Meteor Bot started");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.isFromType(ChannelType.TEXT)) return;

        String msg = event.getMessage().getContentRaw();
        if (!msg.startsWith("<") || !event.getMessage().getMentions().isMentioned(JDA.getSelfUser())) return;

        String rest = msg.substring(22);

        for (String hello : HELLOS) {
            if (rest.equalsIgnoreCase(hello)) {
                event.getChannel().sendMessage(hello + " " + event.getAuthor().getAsMention() + " " + UWUCAT.getFormatted()).queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        Utils.apiPost("discord/userJoined")
            .queryString("id", event.getMember().getId())
            .asEmpty();
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        Utils.apiPost("discord/userLeft").asEmpty();
    }
}
