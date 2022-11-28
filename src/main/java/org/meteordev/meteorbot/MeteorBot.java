package org.meteordev.meteorbot;

import io.github.cdimascio.dotenv.Dotenv;
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
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.meteordev.meteorbot.command.Commands;
import org.slf4j.Logger;

public class MeteorBot extends ListenerAdapter {
    public static String DISCORD_TOKEN, BACKEND_TOKEN, UPTIME_URL; // Private env vars
    public static String API_BASE, GUILD_ID, MEMBER_COUNT_ID, DOWNLOAD_COUNT_ID, COPE_NN_ID; // Public env vars
    private static final String[] HELLOS = { "hi", "hello", "howdy", "bonjour", "ciao", "hej", "hola" };

    public static final Logger LOG = JDALogger.getLog("Meteor Bot");

    public static JDA BOT;
    public static Guild SERVER;
    public static RichCustomEmoji COPE_NN;

    static {
        Dotenv privateEnv = Dotenv.load();
        DISCORD_TOKEN = privateEnv.get("DISCORD_TOKEN");
        BACKEND_TOKEN = privateEnv.get("BACKEND_TOKEN");
        UPTIME_URL = privateEnv.get("UPTIME_URL");

        Dotenv publicEnv = Dotenv.configure().filename("public.env").load();
        API_BASE = publicEnv.get("API_BASE") == null ? "https://meteorclient.com/api/" : publicEnv.get("API_BASE");
        GUILD_ID = publicEnv.get("GUILD_ID");
        MEMBER_COUNT_ID = publicEnv.get("MEMBER_COUNT_ID");
        DOWNLOAD_COUNT_ID = publicEnv.get("DOWNLOAD_COUNT_ID");
        COPE_NN_ID = publicEnv.get("COPE_NN_ID");
    }

    public static void main(String[] args) {
        if (DISCORD_TOKEN == null) {
            MeteorBot.LOG.error("Must specify discord bot token.");
            System.exit(0);
        }

        JDABuilder.createDefault(DISCORD_TOKEN)
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableCache(CacheFlag.EMOJI)
            .addEventListeners(new MeteorBot(), new Commands(), new Uptime(), new InfoChannels())
            .build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        BOT = event.getJDA();
        BOT.getPresence().setActivity(Activity.playing("Meteor Client"));

        SERVER = BOT.getGuildById(GUILD_ID);
        if (SERVER == null) {
            MeteorBot.LOG.error("Couldn't find the specified server.");
            System.exit(0);
        }

        COPE_NN = SERVER.getEmojiById(COPE_NN_ID);

        SERVER.findMembers(m -> !m.isPending()).onSuccess(res -> LOG.info("Cached all {} members.", res.size()));

        LOG.info("Meteor Bot started");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || !event.isFromGuild() || !event.getGuild().equals(SERVER)) return;

        String content = event.getMessage().getContentRaw();
        if (!content.contains(BOT.getSelfUser().getAsMention())) return;

        boolean found = false;

        for (String hello : HELLOS) {
            if (content.toLowerCase().contains(" %s ".formatted(hello))) {
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
