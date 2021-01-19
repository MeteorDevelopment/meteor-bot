package minegame159.meteorbot.commands.help;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MulticonnectCommand extends Command {
    public MulticonnectCommand() {
        super(Category.Help, "Displays help about multiconnect.", "multiconnect");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Use [Multiconnect](https://www.curseforge.com/minecraft/mc-mods/multiconnect) to connect to servers that use versions older than 1.16.3/4/5.").build()).queue();
    }
}
