package org.meteordev.meteorbot.command.commands;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

public class CapyCommand extends Command {
    public CapyCommand() {
        super("capybara", "pulls up");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Unirest.get("https://api.capy.lol/v1/capybara?json=true").asJsonAsync(response -> {
            event.reply(response.getBody().getObject().getJSONObject("data").getString("url")).queue();
        });
    }
}
