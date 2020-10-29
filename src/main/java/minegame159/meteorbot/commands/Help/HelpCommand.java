package minegame159.meteorbot.commands.Help;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.commands.Commands;
import minegame159.meteorbot.utils.Utils;
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
