package minegame159.meteorbot.commands.tickets;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.tickets.Tickets;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static minegame159.meteorbot.utils.Utils.embed;

public class TicketCommand extends Command {
    public TicketCommand() {
        super(Category.Tickets, "Opens a new ticket.", "ticket");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();

        // Check if user already has an open ticket
        if (Tickets.hasTicket(event.getAuthor())) {
            event.getChannel().sendMessage(embed("You already have an open ticket.").build()).queue();
            return;
        }

        // Create ticket
        Tickets.create(event.getAuthor());
    }
}
