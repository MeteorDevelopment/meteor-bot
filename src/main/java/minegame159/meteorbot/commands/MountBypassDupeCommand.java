package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MountBypassDupeCommand extends Command {
    public MountBypassDupeCommand() {
        super("mountbypassdupe", "donkeydupe");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Mount bypass dupe (aka donkey dupe) requires the server to have a very old version of illegalstacks and thus doesnt work on 99.99%% of servers.").build()).queue();
    }
}
