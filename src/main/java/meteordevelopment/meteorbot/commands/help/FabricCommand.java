package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FabricCommand extends Command {
    public FabricCommand() {
        super(Category.Help, "Displays help about fabric.", "install");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Meteor Client requires Fabric Loader and Fabric API.\n Download the [Fabric Loader](https://fabricmc.net/use) and install and run it.\n Download the [Fabric API](https://modrinth.com/mod/fabric-api) and put it in your .minecraft/mods folder.").build()).queue();
    }
}
