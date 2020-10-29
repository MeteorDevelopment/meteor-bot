package minegame159.meteorbot.commands.Help;

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
        event.getChannel().sendMessage(Utils.embed("Use multiconnect if you want to connect to older servers.\nDownload at https://www.curseforge.com/minecraft/mc-mods/multiconnect").build()).queue();
    }
}
