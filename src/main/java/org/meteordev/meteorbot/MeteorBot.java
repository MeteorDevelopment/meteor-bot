package org.meteordev.meteorbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.meteordev.meteorbot.command.Commands;
import org.slf4j.Logger;

public class MeteorBot extends ListenerAdapter {
    private static final String[] HELLOS = { "hi", "hello", "howdy", "bonjour", "ciao", "hej", "hola", "yo" };

    public static final Logger LOG = JDALogger.getLog("Meteor Bot");
    public static final String BACKEND_TOKEN = System.getenv("BACKEND_TOKEN");

    public static JDA BOT;
    public static Guild SERVER;
    public static RichCustomEmoji COPE_NN;

    public static void main(String[] args) {
        String token = System.getenv("DISCORD_TOKEN");

        if (token == null) {
            MeteorBot.LOG.error("Must specify discord bot token.");
            return;
        }

        JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
            .enableCache(CacheFlag.EMOJI)
            .addEventListeners(new MeteorBot(), new Commands(), new Uptime(), new InfoChannels())
            .build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        BOT = event.getJDA();
        BOT.getPresence().setActivity(Activity.playing("Meteor Client"));

        SERVER = BOT.getGuildById(System.getenv("GUILD_ID"));
        if (SERVER == null) {
            MeteorBot.LOG.error("Couldn't find the specified server.");
            System.exit(0);
        }

        COPE_NN = SERVER.getEmojiById(System.getenv("COPE_NN_ID"));

        LOG.info("Meteor Bot started");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || !event.isFromGuild() || !event.getGuild().equals(SERVER)) return;

        String content = event.getMessage().getContentRaw();
        if (!content.contains(BOT.getSelfUser().getAsMention())) return;

        boolean found = false;

        for (String hello : HELLOS) {
            if (content.toLowerCase().contains(hello)) {
                found = true;
                event.getMessage().reply(hello + " :)").queue();
            }
        }

        if (!found) {
            event.getMessage().addReaction(content.toLowerCase().contains("cope") ? COPE_NN : Emoji.fromUnicode("\uD83D\uDC4B")).queue();
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (BACKEND_TOKEN == null) return;

        Utils.apiPost("discord/userJoined").queryString("id", event.getMember().getId()).asEmpty();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (BACKEND_TOKEN == null) return;

        Utils.apiPost("discord/userLeft").asEmpty();
    }
}
