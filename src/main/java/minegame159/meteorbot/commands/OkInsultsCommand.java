package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.OkInsults;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class OkInsultsCommand extends Command {
    public OkInsultsCommand() {
        super("okinsults");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getChannel().sendMessage(Utils.embed(OkInsults.HELP_STRING).build()).queue();
    }
}
