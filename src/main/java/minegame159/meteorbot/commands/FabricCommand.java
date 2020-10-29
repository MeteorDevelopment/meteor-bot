package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FabricCommand extends Command {
    public FabricCommand() {
        super("fabric");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Meteor Client requires Fabric Loader and Fabric API.\n Download Fabric Loader at https://fabricmc.net/use and install it.\nDownload Fabric API at https://www.curseforge.com/minecraft/mc-mods/fabric-api and put it in mods folder.").build()).queue();
    }
}
