package meteordevelopment.meteorbot.commands.tickets;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.tickets.Ticket;
import meteordevelopment.meteorbot.tickets.Tickets;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static meteordevelopment.meteorbot.utils.Utils.embed;

public class CloseCommand extends Command {
    public CloseCommand() {
        super(Category.Tickets, "Closes the current ticket.", "close");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();

        // Get ticket for this channel
        Ticket ticket = Tickets.get(event.getTextChannel());
        if (ticket == null) {
            event.getChannel().sendMessage(embed("Cannot be used outside of a ticket.").build()).queue();
            return;
        }

        // Close the ticket
        ticket.close();
    }
}
