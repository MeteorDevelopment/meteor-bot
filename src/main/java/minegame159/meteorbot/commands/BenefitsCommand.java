package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BenefitsCommand extends Command {
    public BenefitsCommand() {
        super("benefits");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getChannel().sendMessage(Utils.embed("*Donators get:*\n - Ingame cape\n - Colored name and more kit slots on pvp server").build()).queue();
    }
}
