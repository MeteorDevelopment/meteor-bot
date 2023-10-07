package org.meteordev.meteorbot.command.commands.silly;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

public class CatCommand extends Command {
    public CatCommand() {
        super("cat", "gato");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Unirest.get("https://some-random-api.com/img/cat").asJsonAsync(response -> {
            event.reply(response.getBody().getObject().getString("link")).queue();
        });
    }
}
