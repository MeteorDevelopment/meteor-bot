package meteordevelopment.meteorbot.commands.moderator;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.tickets.Tickets;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static meteordevelopment.meteorbot.utils.Utils.embed;

public class SupportMessageCommand extends Command {
    public SupportMessageCommand() {
        super(Category.Moderator, "Sends a support message. (DON'T USE)", "supportmessage");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        if (!Utils.onlyMod(event)) return;

        event.getChannel().sendMessage(embed("To create a ticket press the reaction below.").build()).queue(message -> {
            message.addReaction("\uD83D\uDCE9").queue();

            Tickets.setSupportMessageId(message.getIdLong());
        });
    }
}
