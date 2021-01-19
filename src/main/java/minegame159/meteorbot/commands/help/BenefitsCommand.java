package minegame159.meteorbot.commands.help;

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
        event.getChannel().sendMessage(Utils.embed("*Donators get:*\n - An in-game cape [here](https://meteorclient.com/account)\n - A colored name and 8 kit slots on the PVP server.\n - A role in the Meteor Discord and access to 3 new channels.").build()).queue();
    }
}
