package org.meteordev.meteorbot.command.commands.silly;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

import java.util.concurrent.ThreadLocalRandom;

public class PandaCommand extends Command {
    public PandaCommand() {
        super("panda", "funny thing");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String animal = ThreadLocalRandom.current().nextBoolean() ? "red_panda" : "panda";

        Unirest.get("https://some-random-api.com/animal/" + animal).asJsonAsync(response -> {
            event.reply(response.getBody().getObject().getString("image")).queue();
        });
    }
}
