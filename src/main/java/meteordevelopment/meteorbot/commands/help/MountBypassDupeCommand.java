package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MountBypassDupeCommand extends Command {
    public MountBypassDupeCommand() {
        super(Category.Help, "Displays help about illegalstacks mount bypass dupe.", "mountbypassdupe");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("The Donkey Dupe/Mount Bypass Dupe requires an **extremely old** version of IllegalStacks (the plugin it works on) and as such will not work on 99% of servers.").build()).queue();
    }
}
