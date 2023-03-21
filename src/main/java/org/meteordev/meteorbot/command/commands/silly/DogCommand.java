package org.meteordev.meteorbot.command.commands.silly;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

public class DogCommand extends Command {
    public DogCommand() {
        super("dog", "dawg");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Unirest.get("https://some-random-api.ml/animal/dog").asJsonAsync(response -> {
            event.reply(response.getBody().getObject().getString("image")).queue();
        });
    }
}
