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
    private static final String[] HELLOS = {"hi", "hello", "howdy", "bonjour", "ciao", "hej", "hola", "yo"};

    public static final Logger LOG = JDALogger.getLog("Meteor Bot");

    private Guild server;
    private RichCustomEmoji copeEmoji;

    public static void main(String[] args) {
        String token = Env.DISCORD_TOKEN.value;
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
        JDA bot = event.getJDA();
        bot.getPresence().setActivity(Activity.playing("Meteor Client"));

        server = bot.getGuildById(Env.GUILD_ID.value);
        if (server == null) {
            MeteorBot.LOG.error("Couldn't find the specified server.");
            System.exit(1);
        }

        copeEmoji = server.getEmojiById(Env.COPE_NN_ID.value);

        LOG.info("Meteor Bot started");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || !event.isFromGuild() || !event.getGuild().equals(server)) return;

        String content = event.getMessage().getContentRaw();
        if (!content.contains(event.getJDA().getSelfUser().getAsMention())) return;

        for (String hello : HELLOS) {
            if (content.toLowerCase().contains(hello)) {
                event.getMessage().reply(hello + " :)").queue();
                return;
            }
        }

        event.getMessage().addReaction(content.toLowerCase().contains("cope") ? copeEmoji : Emoji.fromUnicode("\uD83D\uDC4B")).queue();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (Env.BACKEND_TOKEN.value == null) return;

        Utils.apiPost("discord/userJoined").queryString("id", event.getMember().getId()).asEmpty();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (Env.BACKEND_TOKEN.value == null) return;

        Utils.apiPost("discord/userLeft").asEmpty();
    }
}
