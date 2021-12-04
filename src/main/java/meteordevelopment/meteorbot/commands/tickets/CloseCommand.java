package meteordevelopment.meteorbot.commands.tickets;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.tickets.Ticket;
import meteordevelopment.meteorbot.tickets.Tickets;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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

        // Closing message
        List<String> args = List.of(event.getMessage().getContentRaw().split(" "));
        args.remove(0);
        ticket.user.openPrivateChannel().queue(
            channel -> {
                ticket.sendClosingMessage(channel, event.getAuthor(), String.join(" ", args));
            }
        );

        // Close the ticket
        ticket.close();
    }
}
