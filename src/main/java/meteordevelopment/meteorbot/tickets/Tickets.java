package meteordevelopment.meteorbot.tickets;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.database.Database;
import meteordevelopment.meteorbot.database.documents.DbTicket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bson.Document;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorbot.Utils.embedTitle;

public class Tickets {
    private static final List<Ticket> tickets = new ArrayList<>();
    private static final long SUPPORT_MESSAGE_ID = 862490928173678602L;

    public static void init() {
        MeteorBot.SUPPORT.addReactionById(SUPPORT_MESSAGE_ID, "\uD83D\uDCE9").queue();

        for (Document document : Database.TICKETS.getAll()) {
            tickets.add(new Ticket(new DbTicket(document)));
        }
    }

    public static void onReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getMember().getUser().isBot()) return;

        if (event.getMessageIdLong() == SUPPORT_MESSAGE_ID) {
            if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCE9")) {
                create(event.getUser());
            }

            event.retrieveMessage().complete().removeReaction("\uD83D\uDCE9", event.getUser()).queue();
        } else {
            Ticket ticket = get(event.getChannel());
            if (ticket != null) ticket.onReactionAdd(event);
        }
    }

    public static void create(User user) {
        if (hasTicket(user)) return;

        TextChannel channel = MeteorBot.GUILD.createTextChannel("ticket-" + generateId())
            .setParent(MeteorBot.TICKETS)
            .addRolePermissionOverride(MeteorBot.GUILD.getPublicRole().getIdLong(), 0, Permission.VIEW_CHANNEL.getRawValue())
            .addRolePermissionOverride(MeteorBot.MOD_ROLE.getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), 0)
            .addRolePermissionOverride(MeteorBot.HELPER_ROLE.getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), 0)
            .addMemberPermissionOverride(user.getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), 0)
            .complete();

        channel.sendMessage(user.getAsMention()).queue(message -> message.delete().queue());
        Message message = channel.sendMessage(embedTitle(user.getName() + "'s Ticket", "Use reactions to select what problem you have and if the automated answer helped you. If nothing works you will be able to talk to the helpers.").setFooter("React with ❌ to close this ticket.").build()).complete();

        message.addReaction("❌").queue();

        Ticket ticket = new Ticket(user, channel, message);
        tickets.add(ticket);

        Database.TICKETS.add(new DbTicket(ticket));
    }

    private static String generateId() {
        return String.format("%05d", new SecureRandom().nextInt(100000));
    }

    public static Ticket get(TextChannel channel) {
        for (Ticket ticket : tickets) {
            if (ticket.channel.getIdLong() == channel.getIdLong()) return ticket;
        }

        return null;
    }

    public static boolean hasTicket(User user) {
        for (Ticket ticket : tickets) {
            if (ticket.user.getIdLong() == user.getIdLong()) return true;
        }

        return false;
    }

    static void remove(Ticket ticket) {
        tickets.remove(ticket);
        Database.TICKETS.removeAll(ticket.user.getId());
    }
}
