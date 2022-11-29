package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;


public class LogsCommand extends Command {
    private static final String message = "In order to help you, we need your logs! See <https://meteorclient.com/faq/getting-log> if you don't know how to find them.";

    public LogsCommand() {
        super("logs", "tells people how to send logs");
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
