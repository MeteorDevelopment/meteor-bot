package minegame159.meteorbot.commands.tickets;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.tickets.Ticket;
import minegame159.meteorbot.tickets.Tickets;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static minegame159.meteorbot.utils.Utils.embed;

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
