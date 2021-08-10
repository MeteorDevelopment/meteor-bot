package meteordevelopment.meteorbot;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import meteordevelopment.meteorbot.commands.Commands;
import meteordevelopment.meteorbot.database.Db;
import meteordevelopment.meteorbot.tickets.Tickets;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
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
    public static Role MOD_ROLE, HELPER_ROLE, DONATOR_ROLE;

    public static Emote UWUCAT;

    private static final String[] HELLOS = { "Hi", "Hello", "Helo", "Hewo", "Hewwo" };

    public static void main(String[] args) {
        try {
            Config.init();
            Db.init();
            Commands.init();

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
        MOD_ROLE = GUILD.getRoleById(689197893340758022L);
        HELPER_ROLE = GUILD.getRoleById(799392357157830657L);
        DONATOR_ROLE = GUILD.getRoleById(689205464574984353L);

        UWUCAT = GUILD.retrieveEmoteById(806473609526509578L).complete();

        Tickets.init();
        InfoChannels.init();

        LOG.info("Meteor Bot started");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.isFromType(ChannelType.TEXT)) return;

        if (helloMessage(event)) return;

        Commands.onMessage(event);
        Tickets.onMessage(event);
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (!message.startsWith(".link ")) return;

        String[] split = message.split(" ");
        String token = null;

        if (split.length > 1) {
            token = split[1];
        }

        if (token == null) return;

        JSONObject json = Unirest.post("https://meteorclient.com/api/account/linkDiscord")
            .header("Authorization", Config.TOKEN)
            .queryString("id", event.getAuthor().getId())
            .queryString("token", token)
            .asJson().getBody().getObject();

        if (json.has("error")) {
            event.getChannel().sendMessage("Failed to link your Discord account. Try generating a new token by refreshing the account page and clicking the link button again.").queue();
        }
        else {
            event.getChannel().sendMessage("Successfully linked your Discord account.").queue();
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        Tickets.onReactionAdd(event);
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        Unirest.post("https://meteorclient.com/api/discord/userJoined")
                .header("Authorization", Config.TOKEN)
                .queryString("id", event.getMember().getId())
                .asEmpty();
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        Unirest.post("https://meteorclient.com/api/discord/userLeft")
                .header("Authorization", Config.TOKEN)
                .asEmpty();
    }

    private boolean helloMessage(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (!msg.startsWith("<@!742092137218179172> ")) return false;

        String rest = msg.substring(23);

        for (String hello : HELLOS) {
            if (rest.equalsIgnoreCase(hello)) {
                event.getChannel().sendMessage(hello + " " + event.getAuthor().getAsMention() + " " + UWUCAT.getAsMention()).queue();
                return true;
            }
        }

        return false;
    }
}
