package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

public class FaqCommand extends Command {
    public FaqCommand() {
        super("faq", "read the faq");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.USER, "member", "The member to ping.", false);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String message = "Your question is answered by our FAQ, please take the time to thoroughly read through it before coming back.\n<https://meteorclient.com/faq>";

        Member member = parseMember(event);
        if (member != null) {
            message = member.getAsMention() + " " + message;
        }

        event.reply(message).queue();
    }
}
