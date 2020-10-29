package minegame159.meteorbot.commands.other;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class OkInsultCommand extends Command {
    private String name;

    public OkInsultCommand(String name) {
        super(Category.Other, "Ok insult.", name);

        this.name = name;
        if (name.equals("oksquid")) this.name += ".gif";
        else this.name += ".PNG";
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage("https://meteorclient.com/okinsults/" + name).queue();
    }
}
