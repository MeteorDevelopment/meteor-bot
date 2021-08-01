package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.commands.Commands;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(Category.Help, "Sends you this message.", "help");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getAuthor().openPrivateChannel().complete().sendMessage(Utils.embed(Commands.HELP).build()).queue();
    }
}
