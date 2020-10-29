package minegame159.meteorbot.commands.Help;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FabricCommand extends Command {
    public FabricCommand() {
        super(Category.Help, "Displays help about fabric.", "fabric");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Meteor Client requires Fabric Loader and Fabric API.\n Download Fabric Loader at https://fabricmc.net/use and install it.\nDownload Fabric API at https://www.curseforge.com/minecraft/mc-mods/fabric-api and put it in mods folder.").build()).queue();
    }
}
