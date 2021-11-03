package meteordevelopment.meteorbot.tickets;

import meteordevelopment.meteorbot.database.Db;
import meteordevelopment.meteorbot.database.documents.DbTicket;
import meteordevelopment.meteorbot.MeteorBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import static com.mongodb.client.model.Updates.set;
import static meteordevelopment.meteorbot.utils.Utils.embed;
import static meteordevelopment.meteorbot.utils.Utils.embedTitle;

public class Ticket {
    public static final Problem[] PROBLEMS = {
            new Problem(
                    "Can I use meteor for older versions? (1.17/1.16.5/1.12.2/1.8.9)",
                    "Meteor is written for the latest stable release of Minecraft. You can use [Multiconnect](https://modrinth.com/mod/multiconnect) if you want to connect to servers running other versions of Minecraft."
            ),
            new Problem(
                    "Can I use OptiFine with Meteor?",
                    "OptiFine is a Forge mod. There is a Fabric wrapper for it but it does not work well with other Fabric mods and will never work with Meteor. Here are [OptiFine alternatives](https://gist.github.com/LambdAurora/1f6a4a99af374ce500f250c6b42e8754) for Fabric."
            ),
            new Problem(
                    "How do I dupe with Meteor?",
                    "Finding dupes isn't an easy task. Public dupes get patched very quickly so alot of dupes are kept private. For this reason, Meteor has no built in duping modules/features."
            ),
            new Problem(
                    "Does this client have New Chunks?",
                    "No. New Chunks has been patched on any version after 1.12.2."
            ),
            new Problem(
                    "How do I download this client?",
                    "Follow the guide on the [Meteor FAQ](https://github.com/MeteorDevelopment/meteor-client/wiki/Installation)."
            ),
            new Problem(
                    "How to use the proxy system?",
                    "It works just like a normal socks proxy. If you don't know what a proxy is, then I suggest you [google it](https://letmegooglethat.com/?q=proxy) and find out."
            ),
            new Problem(
                    "Why can't I talk in the Meteor Discord?",
                    "Due to the amount of users in the discord server increasing, public channels have been closed to only users who have roles. You can get a role by creating an account [here](https://meteorclient.com/account)"
            )
    };

    public enum Stage {
        Problem,
        Solution,
        Manual
    }

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
        user = MeteorBot.JDA.retrieveUserById(ticket.user).complete();
        channel = MeteorBot.GUILD.getTextChannelById(ticket.channel);

        welcomeMessage = channel.retrieveMessageById(ticket.welcomeMessage).complete();

        stage = Stage.valueOf(ticket.stage);

        problemI = ticket.problem;
        problem = PROBLEMS[problemI];

        if (ticket.message != 0) message = channel.retrieveMessageById(ticket.message).complete();
    }

    public void onMessage(MessageReceivedEvent event) {
        if (stage != Stage.Manual) event.getMessage().delete().queue();
    }

    public void onReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getReactionEmote().isEmote()) {
            event.getReaction().clearReactions().queue();
            return;
        }
        String emoji = event.getReactionEmote().getEmoji();

        if (event.getMessageIdLong() == welcomeMessage.getIdLong()) {
            if (emoji.equals("❌")) close();
        }
        else if (message != null && event.getMessageIdLong() == message.getIdLong()) {
            if (stage == Stage.Problem) {
                int i = emojiToInt(event.getReactionEmote().getName());

                if (i == 0) setStage(Stage.Manual);
                else {
                    problemI = i - 1;
                    problem = PROBLEMS[problemI];

                    Db.TICKETS.update(user.getId(), set("problem", problemI));

                    setStage(Stage.Solution);
                }
            }
            else if (stage == Stage.Solution) {
                if (emoji.equals("✅")) close();
                else if (emoji.equals("❌")) setStage(Stage.Manual);
            }
        }
    }

    private void setStage(Stage stage) {
        this.stage = stage;
        Db.TICKETS.update(user.getId(), set("stage", stage.name()));

        if (message != null) {
            message.delete().queue();
            setMessage(null);
        }

        if (stage == Stage.Problem) {
            sendProblems();
        }
        else if (stage == Stage.Solution) {
            problem.sendSolution(this);
        }
        else if (stage == Stage.Manual) {
            channel.sendMessage(embed("You can now talk to the helpers.").build()).queue();
        }
    }

    private void sendProblems() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < PROBLEMS.length; i++) {
            if (i > 0) sb.append('\n');
            sb.append(intToEmoji(i + 1)).append(" - ").append(PROBLEMS[i].msg);
        }

        Message message1 = channel.sendMessage(embedTitle("Problems", sb.toString()).setFooter("React with the number to get a solution or with ❌ to talk to the helpers.").build()).complete();
        for (int i = 0; i < PROBLEMS.length; i++) {
            message1.addReaction(intToEmoji(i + 1)).queue();
        }

        message1.addReaction("❌").queue();

        setMessage(message1);
    }

    public void setMessage(Message message) {
        this.message = message;

        Db.TICKETS.update(user.getId(), set("message", message == null ? 0 : message.getIdLong()));
    }

    private String intToEmoji(int i) {
        switch (i) {
            case 1:  return "1️⃣";
            case 2:  return "2️⃣";
            case 3:  return "3️⃣";
            case 4:  return "4️⃣";
            case 5:  return "5️⃣";
            case 6:  return "6️⃣";
            case 7:  return "7️⃣";
            case 8:  return "8️⃣";
            case 9:  return "9️⃣";
            default: return "";
        }
    }

    private int emojiToInt(String name) {
        switch (name) {
            case "1️⃣": return 1;
            case "2️⃣": return 2;
            case "3️⃣": return 3;
            case "4️⃣": return 4;
            case "5️⃣": return 5;
            case "6️⃣": return 6;
            case "7️⃣": return 7;
            case "8️⃣": return 8;
            case "9️⃣": return 9;
            default:   return 0;
        }
    }

    public void close() {
        channel.delete().queue();
        Tickets.remove(this);
    }
}
