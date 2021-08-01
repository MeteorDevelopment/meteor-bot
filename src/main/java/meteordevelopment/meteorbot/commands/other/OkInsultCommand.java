package meteordevelopment.meteorbot.commands.other;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class OkInsultCommand extends Command {
    private String name;

    public OkInsultCommand(String name) {
        super(Category.Other, "Ok insult.", name);

        this.name = name;
        if (name.equals("oksquid")) this.name += ".gif";
        else this.name += ".png";
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage("https://meteorclient.com/okinsults/" + name).queue();
    }
}
