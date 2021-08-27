package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MulticonnectCommand extends Command {
    public MulticonnectCommand() {
        super(Category.Help, "Displays help about multiconnect.", "multiconnect");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Use [Multiconnect](https://modrinth.com/mod/multiconnect) to connect to servers that run on older Minecraft versions.").build()).queue();
    }
}
