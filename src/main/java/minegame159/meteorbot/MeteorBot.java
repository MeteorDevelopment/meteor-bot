package minegame159.meteorbot;

import minegame159.meteorbot.commands.*;
import minegame159.meteorbot.commands.account.*;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MeteorBot extends ListenerAdapter {
    public static final Logger LOG = JDALogger.getLog("MeteorBot");

    private static final Pattern NIGGER_PATTERN = Pattern.compile("nigger", Pattern.CASE_INSENSITIVE);
    private static final Pattern NIGGA_PATTERN = Pattern.compile("nigga", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        try {
            JDABuilder.createDefault("NzQyMDkyMTM3MjE4MTc5MTcy.XzBFKA.oRU_nMfuMgXGXFvmwSLBRD_H-0s")
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new MeteorBot())
                    .build();

        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private final List<Command> commands = new ArrayList<>();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing("Meteor on Crack!"));

        OkInsults.init(commands);

        commands.add(new NWordCommand());
        commands.add(new NiggerboardCommand());
        commands.add(new StatsCommand());
        commands.add(new ResetNWordsCommand());
        commands.add(new AirhornCommand());
        commands.add(new MulticonnectCommand());
        commands.add(new FabricCommand());
        commands.add(new BaritoneCommand());
        commands.add(new FpsCommand());
        commands.add(new MountBypassDupeCommand());
        commands.add(new BenefitsCommand());

        commands.add(new LinkCommand());
        commands.add(new UnlinkCommand());
        commands.add(new InfoCommand());
        commands.add(new MaxMcAccountsCommand());
        commands.add(new DonatorCommand());

        Db.init();
        Audio.init();

        LOG.info("Meteor Bot started");
        LOG.info("Loaded {} commands", commands.size());
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.isFromType(ChannelType.TEXT)) return;
        String str = event.getMessage().getContentRaw();

        // Commands
        if (str.startsWith(".")) {
            String name = str.substring(1).split(" ")[0];

            for (Command command : commands) {
                if (command.isName(name)) {
                    command.run(event);
                    return;
                }
            }
        }

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
        Utils.updateMemberCount(event.getGuild(), true);
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        Utils.updateMemberCount(event.getGuild(), false);
    }
}
