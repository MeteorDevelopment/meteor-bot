package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BaritoneCommand extends Command {
    public BaritoneCommand() {
        super("baritone");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Meteor Client has Baritone built in. (Fabritone)\nFabritone's default prefix is '@' but in Meteor you can use 'b' command.\nFor all Baritone commands visit https://github.com/cabaletta/baritone/blob/master/USAGE.md").build()).queue();
    }
}
