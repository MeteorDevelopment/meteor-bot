package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;


public class FaqCommand extends Command {
    public FaqCommand() {
        super("faq", "read the faq");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyEmbeds(Utils.embed("Your question is answered in our [faq](https://meteorclient.com/faq), please take the time to thoroughly read through it before coming back here saying it wasn't answered there\nIf this embed was sent, it is very likely answered there").build()).queue();
    }
}
