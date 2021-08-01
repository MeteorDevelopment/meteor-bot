package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BaritoneCommand extends Command {
    public BaritoneCommand() {
        super(Category.Help, "Displays help about baritone.", "baritone");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Meteor Client has Baritone built in.\nBaritone's default prefix is '#' but in Meteor you can use 'b' command.\nFor all Baritone commands visit [Baritone's Usage Page](https://github.com/cabaletta/baritone/blob/master/USAGE.md).").build()).queue();
    }
}
