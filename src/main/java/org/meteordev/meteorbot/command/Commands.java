package org.meteordev.meteorbot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.meteordev.meteorbot.Env;
import org.meteordev.meteorbot.MeteorBot;
import org.meteordev.meteorbot.command.commands.LinkCommand;
import org.meteordev.meteorbot.command.commands.StatsCommand;
import org.meteordev.meteorbot.command.commands.help.FaqCommand;
import org.meteordev.meteorbot.command.commands.help.InstallationCommand;
import org.meteordev.meteorbot.command.commands.help.LogsCommand;
import org.meteordev.meteorbot.command.commands.help.OldVersionCommand;
import org.meteordev.meteorbot.command.commands.moderation.BanCommand;
import org.meteordev.meteorbot.command.commands.moderation.CloseCommand;
import org.meteordev.meteorbot.command.commands.moderation.MuteCommand;
import org.meteordev.meteorbot.command.commands.moderation.UnmuteCommand;
import org.meteordev.meteorbot.command.commands.silly.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands extends ListenerAdapter {
    private static final Map<String, Command> BOT_COMMANDS = new HashMap<>();

    public static void add(Command command) {
        BOT_COMMANDS.put(command.name, command);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        add(new FaqCommand());
        add(new InstallationCommand());
        add(new LogsCommand());
        add(new OldVersionCommand());

        add(new BanCommand());
        add(new CloseCommand());
        add(new MuteCommand());
        add(new UnmuteCommand());

        add(new CapyCommand());
        add(new CatCommand());
        add(new DogCommand());
        add(new MonkeyCommand());
        add(new PandaCommand());

        add(new StatsCommand());
        if (Env.BACKEND_TOKEN.value != null) {
            add(new LinkCommand());
        }

        List<CommandData> commandData = new ArrayList<>();

        for (Command command : BOT_COMMANDS.values()) {
            commandData.add(command.build(new CommandDataImpl(command.name, command.description)));
        }

        event.getJDA().updateCommands().addCommands(commandData).complete();

        MeteorBot.LOG.info("Loaded {} commands", BOT_COMMANDS.size());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Command command = BOT_COMMANDS.get(event.getName());
        if (command == null) return;

        command.run(event);
    }
}
