package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;


public class OldVersionCommand extends Command {
    private static final String message = "We **do not** support old versions of Meteor **or** Minecraft **no matter what!**\nIf you want to play on older servers, use ViaFabric or MultiConnect with the latest version of Meteor.\n<https://modrinth.com/mod/viafabric>\n<https://modrinth.com/mod/multiconnect>";

    public OldVersionCommand() {
        super("old-version", "we don't support old versions");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.USER, "member", "The member to ping.", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Member member = parseMember(event);
        if (member == null) return;

        event.reply("%s %s".formatted(member.getAsMention(), message)).queue();
    }
}
