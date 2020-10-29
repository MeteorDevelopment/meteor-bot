package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LinkedCommand extends Command {
    public LinkedCommand() {
        super("linked");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        User user = Db.USERS.get(event.getAuthor());

        if (user == null || user.mcAccounts.isEmpty()) {
            event.getChannel().sendMessage(Utils.embed("You haven't linked any minecraft accounts yet, do `.link <username>`.").build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        boolean update = false;
        boolean found = false;

        sb.append("*").append("You have linked ").append(user.mcAccounts.size()).append(" out of maximum ").append(user.maxMcAccounts).append(" ").append(user.maxMcAccounts == 1 ? "account" : "accounts").append(".*\n");

        for (int i = 0; i < user.mcAccounts.size(); i++) {
            String username = Utils.getMcUsername(user.mcAccounts.get(i));

            if (username == null) {
                user.mcAccounts.remove(i);
                i--;
                update = true;
                continue;
            }

            sb.append("\n**").append(i + 1).append("**. ").append(username);

            found = true;
        }

        if (update) Db.USERS.update(user);
        if (found) event.getChannel().sendMessage(Utils.embed(sb.toString()).build()).queue();
    }
}
