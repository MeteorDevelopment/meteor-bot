package org.meteordev.meteorbot.command.commands.help;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import org.meteordev.meteorbot.command.Command;

public abstract class HelpCommand extends Command {
    protected final ItemComponent[] components;
    protected final String message;

    protected HelpCommand(String name, String description, String message, ItemComponent... components) {
        super(name, description);

        this.message = message;
        this.components = components == null ? new ItemComponent[0] : components;
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.USER, "member", "The member to ping.", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Member member = parseMember(event);
        if (member == null) return;

        event.reply("%s %s".formatted(member.getAsMention(), message)).addActionRow(components).queue();
    }
}
