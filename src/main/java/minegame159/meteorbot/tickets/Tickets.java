package minegame159.meteorbot.tickets;

import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.DbTicket;
import minegame159.meteorbot.database.documents.Stats;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.set;
import static minegame159.meteorbot.utils.Utils.embedTitle;

public class Tickets {
    private static long SUPPORT_MESSAGE_ID = 0;

    private static Category CATEGORY;

    private static final List<Ticket> tickets = new ArrayList<>();

    public static void init() {
        CATEGORY = MeteorBot.GUILD.getCategoryById(799221880958615552L);

        for (Document document : Db.TICKETS.getAll()) {
            tickets.add(new Ticket(new DbTicket(document)));
        }

        SUPPORT_MESSAGE_ID = Db.GLOBAL.get(Stats.class, Stats.ID).supportMessage;
    }

    public static void setSupportMessageId(long id) {
        SUPPORT_MESSAGE_ID = id;

        Db.GLOBAL.update(Stats.ID, set("supportMessage", SUPPORT_MESSAGE_ID));
    }

    public static void onMessage(MessageReceivedEvent event) {
        Ticket ticket = get(event.getTextChannel());
        if (ticket == null || event.getAuthor().isBot()) return;

        ticket.onMessage(event);
    }

    public static void onReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getMember().getUser().isBot()) return;

        if (event.getMessageIdLong() == SUPPORT_MESSAGE_ID) {
            if (event.getReactionEmote().isEmote()) {
                event.getReaction().clearReactions().queue();
            }
            else if (event.getReactionEmote().getEmoji().equals("\uD83D\uDCE9")) {
                create(event.getUser());
            }
            else {
                event.getReaction().clearReactions().queue();
            }
        } else {
            Ticket ticket = get(event.getChannel());
            if (ticket != null) ticket.onReactionAdd(event);
        }
    }

    public static void create(User user) {
        if (hasTicket(user)) return;

        TextChannel channel = MeteorBot.GUILD.createTextChannel("ticket-" + Utils.generateToken(true))
                .setParent(CATEGORY)
                .addRolePermissionOverride(MeteorBot.GUILD.getPublicRole().getIdLong(), 0, Permission.VIEW_CHANNEL.getRawValue())
                .addRolePermissionOverride(MeteorBot.MOD_ROLE.getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), 0)
                .addMemberPermissionOverride(user.getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), 0)
                .complete();

        Message message = channel.sendMessage(embedTitle("Meteor Tickets", "Use reactions to select what problem you have and if the automated answer helped you. If nothing works you will be able to talk to the moderators.").setFooter("React with ❌ to close this ticket.").build()).complete();

        message.addReaction("❌").queue();

        Ticket ticket = new Ticket(user, channel, message);
        tickets.add(ticket);

        Db.TICKETS.add(new DbTicket(ticket));
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

        Db.TICKETS.delete(ticket.user.getId());
    }
}
