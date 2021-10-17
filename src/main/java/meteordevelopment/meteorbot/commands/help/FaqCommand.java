package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FaqCommand extends Command {
    public FaqCommand() {
        super(Category.Help, "Displays help about the FAQ", "faq");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Checkout the [FAQ](https://github.com/MeteorDevelopment/meteor-client/wiki) page for frequently asked questions.").build()).queue();
    }
}
