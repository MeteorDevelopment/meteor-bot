package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("info");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        User user = Db.USERS.get(event.getAuthor());
        if (user == null) {
            user = new User(event.getAuthor());
            Db.USERS.add(user);
        }

        StringBuilder sb = new StringBuilder("**Donator:** %s\n**Cape:** %s\n**Max mc accounts:** %d");
        if (user.mcAccounts.isEmpty()) sb.append("\n\n*You have no minecraft accounts linked, do `.link <username>`.*");

        event.getChannel().sendMessage(Utils.embed(sb.toString(), user.donator, user.cape, user.maxMcAccounts).build()).queue();
    }
}
