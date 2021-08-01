package meteordevelopment.meteorbot.database.documents;

import meteordevelopment.meteorbot.database.ISerializable;
import meteordevelopment.meteorbot.tickets.Ticket;
import org.bson.Document;

public class DbTicket implements ISerializable {
    public String user;
    public long channel;

    public long welcomeMessage;

    public String stage;

    public int problem;

    public long message;

    public DbTicket(Ticket ticket) {
        user = ticket.user.getId();
        channel = ticket.channel.getIdLong();

        welcomeMessage = ticket.welcomeMessage.getIdLong();

        stage = ticket.stage.name();

        problem = ticket.problemI;

        if (ticket.message != null) message = ticket.message.getIdLong();
    }

    public DbTicket(Document document) {
        user = document.getString("id");
        channel = document.getLong("channel");

        welcomeMessage = document.getLong("welcomeMessage");

        stage = document.getString("stage");

        problem = document.getInteger("problem");

        message = document.getLong("message");
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", user)
                .append("channel", channel)
                .append("welcomeMessage", welcomeMessage)
                .append("stage", stage)
                .append("problem", problem)
                .append("message", message);
    }

    @Override
    public String getId() {
        return user;
    }
}
