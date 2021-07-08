package meteordevelopment.meteorbot.tickets;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.database.Database;
import meteordevelopment.meteorbot.database.documents.DbTicket;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import static com.mongodb.client.model.Updates.set;
import static meteordevelopment.meteorbot.Utils.embedTitle;
import static meteordevelopment.meteorbot.tickets.Tickets.remove;

public class Ticket {
    public static final Problem[] PROBLEMS = {
        new Problem(
            "How can I install Meteor?",
            "Follow the guide on the [FAQ](https://github.com/MeteorDevelopment/meteor-client/wiki/Installation)."
        ),
        new Problem(
            "Will there be a 1.12.2 or Forge version?",
            "No, there never will be. Use the `.multiconnect` command for more info on connecting to older servers."
        ),
        new Problem(
            "Can I use Optifine with Meteor?",
            "No, Optifine is a Forge mod. There is a Fabric wrapper for it but it does not work well with other Fabric mods. Use the `.fps` command for more info on boosting fps on Fabric."
        )
    };

    public final User user;
    public final TextChannel channel;
    public final Message welcomeMessage;
    public Stage stage;
    public int problemI;
    public Problem problem;
    public Message message;

    public Ticket(User user, TextChannel channel, Message welcomeMessage) {
        this.user = user;
        this.channel = channel;
        this.welcomeMessage = welcomeMessage;

        setStage(Stage.Problem);
    }

    public Ticket(DbTicket ticket) {
        user = MeteorBot.CORE.retrieveUserById(ticket.user).complete();
        channel = MeteorBot.GUILD.getTextChannelById(ticket.channel);

        welcomeMessage = channel.retrieveMessageById(ticket.welcomeMessage).complete();

        stage = Stage.valueOf(ticket.stage);

        problemI = ticket.problem;
        problem = PROBLEMS[problemI];

        if (ticket.message != 0) message = channel.retrieveMessageById(ticket.message).complete();
    }

    public void onReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getReactionEmote().isEmote()) {
            event.getReaction().removeReaction().queue();
            return;
        }

        String emoji = event.getReactionEmote().getEmoji();

        if (event.getMessageIdLong() == welcomeMessage.getIdLong()) {
            if (emoji.equals("❌")) close();
        } else if (message != null && event.getMessageIdLong() == message.getIdLong()) {
            if (stage == Stage.Problem) {
                int i = emojiToInt(event.getReactionEmote().getName());

                if (i != 0) {
                    problemI = i - 1;
                    problem = PROBLEMS[problemI];

                    Database.TICKETS.update(user.getId(), set("problem", problemI));

                    setStage(Stage.Solution);
                } else if (emoji.equals("❌")) {
                    setStage(Stage.Manual);
                }
            } else if (stage == Stage.Solution) {
                if (emoji.equals("✅")) close();
            }
        }
    }

    private void setStage(Stage stage) {
        this.stage = stage;
        Database.TICKETS.update(user.getId(), set("stage", stage.name()));

        if (message != null) {
            message.delete().queue();
            setMessage(null);
        }

        if (stage == Stage.Problem) {
            sendProblems();
        } else if (stage == Stage.Solution) {
            problem.sendSolution(this);
        }
    }

    private void sendProblems() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < PROBLEMS.length; i++) {
            if (i > 0) sb.append('\n');
            sb.append(intToEmoji(i + 1)).append(" - ").append(PROBLEMS[i].message);
        }

        Message message1 = channel.sendMessage(embedTitle("Problems", sb.toString()).setFooter("React to a problem to get the solution or with ❌ to talk to the helpers.").build()).complete();
        for (int i = 0; i < PROBLEMS.length; i++) {
            message1.addReaction(intToEmoji(i + 1)).queue();
        }

        message1.addReaction("❌").queue();

        setMessage(message1);
    }

    public void setMessage(Message message) {
        this.message = message;

        Database.TICKETS.update(user.getId(), set("message", message == null ? 0 : message.getIdLong()));
    }

    private String intToEmoji(int i) {
        return switch (i) {
            case 1 -> "1️⃣";
            case 2 -> "2️⃣";
            case 3 -> "3️⃣";
            case 4 -> "4️⃣";
            case 5 -> "5️⃣";
            case 6 -> "6️⃣";
            case 7 -> "7️⃣";
            case 8 -> "8️⃣";
            case 9 -> "9️⃣";
            default -> "";
        };
    }

    private int emojiToInt(String name) {
        return switch (name) {
            case "1️⃣" -> 1;
            case "2️⃣" -> 2;
            case "3️⃣" -> 3;
            case "4️⃣" -> 4;
            case "5️⃣" -> 5;
            case "6️⃣" -> 6;
            case "7️⃣" -> 7;
            case "8️⃣" -> 8;
            case "9️⃣" -> 9;
            default -> 0;
        };
    }

    public void close() {
        channel.delete().queue();
        remove(this);
    }

    public enum Stage {
        Problem,
        Solution,
        Manual
    }
}
