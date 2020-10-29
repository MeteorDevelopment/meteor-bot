package minegame159.meteorbot.commands.Help;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BenefitsCommand extends Command {
    public BenefitsCommand() {
        super(Category.Help, "Displays what benefits donators get.", "benefits");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getChannel().sendMessage(Utils.embed("*Donators get:*\n - Ingame cape\n - Colored name and more kit slots on pvp server").build()).queue();
    }
}
