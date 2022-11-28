package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;


public class InstallationCommand extends Command {
    public InstallationCommand() {
        super("installation", "tells people how to install meteor");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.USER, "member", "The member to ping.", false);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String message = "Please read our guide for installing meteor.\n<https://meteorclient.com/faq/installation>";

        Member member = parseMember(event);
        if (member != null) {
            message = member.getAsMention() + " " + message;
        }

        event.reply(message).queue();
    }
}
