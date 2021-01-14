package minegame159.meteorbot.commands.Help;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BaritoneCommand extends Command {
    public BaritoneCommand() {
        super(Category.Help, "Displays help about baritone.", "baritone");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Meteor Client has Baritone built in. (Fabritone)\nFabritone's default prefix is '@' but in Meteor you can use the '.b' command.\nFor all Baritone commands visit https://github.com/cabaletta/baritone/blob/master/USAGE.md").build()).queue();
    }
}
