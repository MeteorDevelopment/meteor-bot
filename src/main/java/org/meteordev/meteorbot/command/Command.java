package org.meteordev.meteorbot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {
    public final String name, description;

    protected Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract SlashCommandData build(SlashCommandData data);

    public abstract void run(SlashCommandInteractionEvent event);

    public static Member parseMember(SlashCommandInteractionEvent event) {
        OptionMapping memberOption = event.getOption("member");
        if (memberOption == null || memberOption.getAsMember() == null) {
            event.reply("Couldn't find that member.").setEphemeral(true).queue();
            return null;
        }

        Member member = memberOption.getAsMember();
        if (member == null) event.reply("Couldn't find that member.").setEphemeral(true).queue();

        return member;
    }
}
