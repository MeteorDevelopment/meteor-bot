package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MulticonnectCommand extends Command {
    public MulticonnectCommand() {
        super("multiconnect");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Use multiconnect if you want to connect to older servers.\nDownload at https://www.curseforge.com/minecraft/mc-mods/multiconnect").build()).queue();
    }
}
