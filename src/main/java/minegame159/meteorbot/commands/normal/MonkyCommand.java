package minegame159.meteorbot.commands.normal;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class MonkyCommand extends Command {
    public MonkyCommand() {
        super(Category.Normal, "monky moment", "monky");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();

        int w = ThreadLocalRandom.current().nextInt(200, 1001);
        int h = ThreadLocalRandom.current().nextInt(200, 1001);

        event.getChannel().sendMessage(new EmbedBuilder()
                .setColor(new Color(204, 0,0))
                .setImage("https://www.placemonkeys.com/" + w + "/" + h + "?random")
                .build()).queue();
    }
}
