package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FpsCommand extends Command {
    public FpsCommand() {
        super("fps");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("If you want better performance while playing with Fabric download these 3 mods:\nhttps://www.curseforge.com/minecraft/mc-mods/sodium\nhttps://www.curseforge.com/minecraft/mc-mods/phosphor\nhttps://www.curseforge.com/minecraft/mc-mods/lithium").build()).queue();
    }
}
