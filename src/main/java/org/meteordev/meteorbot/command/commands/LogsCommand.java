package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;


public class LogsCommand extends Command {
    public LogsCommand() {
        super("logs", "tells people how to send logs");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyEmbeds(Utils.embed("In order to be able to help you, we need your logs! follow [this guide](https://meteorclient.com/faq/getting-log) to send us your logs").build()).queue();
    }
}
