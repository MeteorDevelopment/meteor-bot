package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FpsCommand extends Command {
    public FpsCommand() {
        super(Category.Help, "Displays help on how to get more fps.", "fps");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("If you want better performance while playing with Fabric download these 3 mods:\n[Sodium](https://modrinth.com/mod/sodium)\n[Phosphor](https://modrinth.com/mod/phosphor)\n[Lithium](https://modrinth.com/mod/lithium)").build()).queue();
    }
}
