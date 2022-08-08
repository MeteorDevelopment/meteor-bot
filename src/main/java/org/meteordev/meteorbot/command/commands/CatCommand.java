package org.meteordev.meteorbot.command.commands;

import kong.unirest.Unirest;
import org.meteordev.meteorbot.command.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CatCommand extends Command {
    public CatCommand() {
        super("cat", "Sends a cute cat.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Unirest.get("https://aws.random.cat/meow").asJsonAsync(response -> {
            event.reply(response.getBody().getObject().getString("file")).queue();
        });
    }
}
