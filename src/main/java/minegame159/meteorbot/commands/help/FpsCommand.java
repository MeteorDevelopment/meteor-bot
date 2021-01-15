package minegame159.meteorbot.commands.help;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FpsCommand extends Command {
    public FpsCommand() {
        super(Category.Help, "Displays help on how to get more fps.", "fps");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("If you want better performance while playing with Fabric download these 3 mods:\n[Sodium](https://www.curseforge.com/minecraft/mc-mods/sodium)\n[Phosphor](https://www.curseforge.com/minecraft/mc-mods/phosphor)\n[Lithium](https://www.curseforge.com/minecraft/mc-mods/lithium)").build()).queue();
    }
}
