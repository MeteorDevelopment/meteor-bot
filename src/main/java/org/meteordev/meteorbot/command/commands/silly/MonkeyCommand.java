package org.meteordev.meteorbot.command.commands.silly;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

import java.util.concurrent.ThreadLocalRandom;

public class MonkeyCommand extends Command {
    public MonkeyCommand() {
        super("monkey", "Sends a swag monkey.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        int w = ThreadLocalRandom.current().nextInt(200, 1001);
        int h = ThreadLocalRandom.current().nextInt(200, 1001);

        event.reply("https://www.placemonkeys.com/" + w + "/" + h + "?random").queue();
    }
}
