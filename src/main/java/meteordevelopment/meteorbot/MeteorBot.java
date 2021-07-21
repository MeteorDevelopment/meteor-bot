package meteordevelopment.meteorbot;

import meteordevelopment.meteorbot.commands.Commands;
import meteordevelopment.meteorbot.database.Database;
import meteordevelopment.meteorbot.tickets.Tickets;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeteorBot extends ListenerAdapter {
    private static final String[] HELLOS = {"Hi", "Hello", "Helo", "Hewo", "Hewwo", "Hai", "Ello", "Hey"};

    public static final Logger LOG = JDALogger.getLog("MeteorBot");

    // Discord stuff
    public static JDA CORE;
    public static Guild GUILD;
    public static GuildChannel DONATOR_CHAT;
    public static TextChannel SUPPORT;
    public static GuildChannel LOGS_CHANNEL;
    public static Category TICKETS;
    public static Role DEV_ROLE, MOD_ROLE, HELPER_ROLE, DONATOR_ROLE, MUTED_ROLE;
    public static Emote UWUCAT, FABRIC, GITHUB, METEOR, MODRINTH;

    public static void main(String[] args) {
        Config.init();

        try {
            JDABuilder.createDefault(Config.DISCORD_TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES)
                .addEventListeners(new MeteorBot())
                .build();
        } catch (LoginException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        CORE = event.getJDA();
        CORE.getPresence().setActivity(Activity.playing("Meteor on Crack!"));
        LOG.info("Meteor Bot started");

        GUILD = CORE.getGuildById(861351437997178950L);

        TICKETS = GUILD.getCategoryById(862470905015566347L);

        DONATOR_CHAT = GUILD.getGuildChannelById(862376330695016488L);
        SUPPORT = GUILD.getTextChannelById(862489308497313822L);
        LOGS_CHANNEL = GUILD.getTextChannelById(861351437997178953L);

        DEV_ROLE = GUILD.getRoleById(862116295272038400L);
        MOD_ROLE = GUILD.getRoleById(862116323387637829L);
        HELPER_ROLE = GUILD.getRoleById(862116346006339604L);
        DONATOR_ROLE = GUILD.getRoleById(862116373785477150L);
        MUTED_ROLE = GUILD.getRoleById(862764287856082985L);

        UWUCAT = GUILD.getEmoteById(862102804931936277L);
        FABRIC = GUILD.getEmoteById(862353758750900274L);
        GITHUB = GUILD.getEmoteById(862363431046873148L);
        METEOR = GUILD.getEmoteById(862363090804932628L);
        MODRINTH = GUILD.getEmoteById(862365311944097812L);

        Database.init();
        Commands.init();
        Tickets.init();
        Mutes.init();
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getChannel().getType() != ChannelType.TEXT || event.getGuild() != MeteorBot.GUILD) return;

        if (!Utils.isStaff(event.getMember()) && inviteFilter(event.getMessage().getContentRaw())) {
            // Logging
            EmbedBuilder logEmbed = new EmbedBuilder()
                .setAuthor(String.format("%s#%s sent an invite in #%s!", event.getMember().getUser().getName(), event.getMember().getUser().getDiscriminator(), event.getChannel().getName()),
                    null, event.getMember().getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR);
            StringBuilder sb = new StringBuilder();
            Matcher matcher = Pattern.compile("(https?://)?(www.)?(discord.(gg|io|me|li)|discordapp.com/invite)/[^\\s/]+?(?=\\b)").matcher(event.getMessage().getContentRaw());
            while (matcher.find()) sb.append(matcher.group()).append("\n");
            logEmbed.addField("**Invites**", sb.toString(), false);

            event.getGuild().getTextChannelById(LOGS_CHANNEL.getId()).sendMessage(logEmbed.build()).queue();


            event.getMessage().delete().queue();
            event.getChannel().sendMessage(event.getMessage().getMember().getAsMention() + " please don't send discord invites here :palm_tree:").queue();
            return;
        }

        if (helloMessage(event)) return;

        Commands.onMessage(event);
    }

    private boolean helloMessage(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (!msg.startsWith(CORE.getSelfUser().getAsMention() + " ")) return false;

        String rest = msg.substring((CORE.getSelfUser().getAsMention() + " ").length());

        for (String hello : HELLOS) {
            if (rest.toLowerCase().contains(hello.toLowerCase())) {
                hello = HELLOS[new Random().nextInt(HELLOS.length)];
                event.getMessage().reply(hello + " " + UWUCAT.getAsMention()).queue();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if (event.getAuthor().isBot() || event.getChannel().getType() != ChannelType.TEXT || event.getGuild() != MeteorBot.GUILD) return;

        if (!Utils.isStaff(event.getMember()) && inviteFilter(event.getMessage().getContentRaw())) {
            event.getMessage().delete().queue();
            event.getChannel().sendMessage(event.getMessage().getMember().getAsMention() + " please don't send discord invites here :palm_tree:").queue();
        }
    }

    private boolean inviteFilter(String content) {
        return !content.equals("discord.gg/meteor") && Pattern.compile("(https?://)?(www.)?(discord.(gg|io|me|li)|discordapp.com/invite)/[^\\s/]+?(?=\\b)").matcher(content).find();
    }

    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (event.getUser().isBot() || event.getChannel().getType() != ChannelType.TEXT || event.getGuild() != MeteorBot.GUILD) return;

        Commands.onSlashCommand(event);
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        Tickets.onReactionAdd(event);
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
//        Utils.updateMemberCount(event.getGuild(), true);
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
//        Utils.updateMemberCount(event.getGuild(), false);
    }
}
