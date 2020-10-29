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

        StringBuilder sb = new StringBuilder("**Donator:** %s\n**Cape:** %s");

        if (user.mcAccounts.isEmpty()) sb.append("\n\n*You have no minecraft accounts linked, do `.link <username>`.*");
        else {
            boolean update = false;

            sb.append("\n\n*").append("You have linked ").append(user.mcAccounts.size()).append(" out of maximum ").append(user.maxMcAccounts).append(" ").append(user.maxMcAccounts == 1 ? "account" : "accounts").append(".*\n");

            for (int i = 0; i < user.mcAccounts.size(); i++) {
                String username = Utils.getMcUsername(user.mcAccounts.get(i));

                if (username == null) {
                    user.mcAccounts.remove(i);
                    i--;
                    update = true;
                    continue;
                }

                sb.append("\n**").append(i + 1).append("**. ").append(username);
            }

            if (update) Db.USERS.update(user);
        }

        event.getChannel().sendMessage(Utils.embed(sb.toString(), Utils.boolToString(user.donator), Utils.boolToString(user.cape), user.maxMcAccounts).build()).queue();
    }
}
