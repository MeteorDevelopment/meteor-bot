package org.meteordev.meteorbot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.meteordev.meteorbot.MeteorBot;
import org.meteordev.meteorbot.command.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands extends ListenerAdapter {
    private static final Map<String, Command> commands = new HashMap<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        add(new CatCommand());
        add(new MonkyCommand());
        add(new StatsCommand());
        add(new LinkCommand());
        add(new MuteCommand());
        add(new UnmuteCommand());
        add(new LogsCommand());
        add(new FaqCommand());
        add(new InstallationCommand());

        List<CommandData> commandData = new ArrayList<>();

        for (Command command : commands.values()) {
            commandData.add(command.build(new CommandDataImpl(command.name, command.description)));
        }

        MeteorBot.JDA.updateCommands().addCommands(commandData).complete();

        MeteorBot.LOG.info("Loaded {} commands", commands.size());
    }

    public static void add(Command command) {
        commands.put(command.name, command);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Command command = commands.get(event.getName());
        if (command == null) return;

        command.run(event);
    }

    public static Member parseMember(SlashCommandInteractionEvent event) {
        OptionMapping memberOption = event.getOption("member");
        if (memberOption == null || memberOption.getAsMember() == null) {
            event.reply("Couldn't find that member.").setEphemeral(true).queue();
            return null;
        }

        Member member = memberOption.getAsMember();
        if (member == null) {
            event.reply("Couldn't find that member.").setEphemeral(true).queue();
        }

        return member;
    }
}
