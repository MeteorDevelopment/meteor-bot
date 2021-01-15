package minegame159.meteorbot.commands.normal;

import kong.unirest.Unirest;
import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CatCommand extends Command {

    public CatCommand() {
        super(Category.Normal, "cat moment", "cat");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        Unirest.get("https://aws.random.cat/meow").asJsonAsync(response -> event.getChannel().sendMessage(new EmbedBuilder()
                .setColor(new Color(204, 0,0))
                .setImage(response.getBody().getObject().getString("file"))
                .build()));
    }
}
