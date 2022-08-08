package org.meteordev.meteorbot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {
    public final String name, description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract SlashCommandData build(SlashCommandData data);

    public abstract void run(SlashCommandInteractionEvent event);
}
