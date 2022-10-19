package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;


public class InstallationCommand extends Command {
    public InstallationCommand() {
        super("installation", "tells people how to install meteor");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyEmbeds(Utils.embed("Please read our [installation guide](https://meteorclient.com/faq/installation) as everything there is to know about installing meteor is explained there, regardless of the information you learned from other sources").build()).queue();
    }
}
