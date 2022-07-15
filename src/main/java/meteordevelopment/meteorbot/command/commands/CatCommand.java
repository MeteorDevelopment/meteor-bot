package meteordevelopment.meteorbot.command.commands;

import kong.unirest.Unirest;
import meteordevelopment.meteorbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CatCommand extends Command {
    public CatCommand() {
        super("cat");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();

        Unirest.get("https://aws.random.cat/meow").asJsonAsync(response -> event.getChannel().sendMessage(new EmbedBuilder()
                .setColor(new Color(204, 0,0))
                .setImage(response.getBody().getObject().getString("file"))
                .build()).queue());
    }
}
