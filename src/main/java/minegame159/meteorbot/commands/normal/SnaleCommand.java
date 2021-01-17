package minegame159.meteorbot.commands.normal;

import kong.unirest.Unirest;
import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class SnaleCommand extends Command {
    public SnaleCommand() {
        super(Category.Normal, "Snale'd ezezzeez.", "snale");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();

        Unirest.get("https://seasnail.xyz/api/snale").asStringAsync(response -> event.getChannel().sendMessage(new EmbedBuilder()
                .setColor(new Color(255, 247,145))
                .setImage(response.getBody())
                .build()
                ).queue()
        );
    }
}
