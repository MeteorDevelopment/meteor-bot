package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.PvpServer;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UnlinkCommand extends Command {
    public UnlinkCommand() {
        super(Category.Account, "Unlinks minecraft account from your account.", "unlink");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        if (split.length < 2) {
            event.getChannel().sendMessage(Utils.embed("Specify your minecraft username, check your linked accounts with `.info`.").build()).queue();
            return;
        }

        String username = split[1];
        User user = Db.USERS.get(event.getAuthor());

        if (user == null || user.mcAccounts.isEmpty()) {
            event.getChannel().sendMessage(Utils.embed("You haven't linked any minecraft accounts yet, do `.link <username>`.").build()).queue();
            return;
        }

        boolean update = false;
        boolean found = false;
        String uuid = null;

        for (int i = 0; i < user.mcAccounts.size(); i++) {
            String name = Utils.getMcUsername(user.mcAccounts.get(i));

            if (username == null) {
                user.mcAccounts.remove(i);
                i--;
                update = true;
                continue;
            }

            if (username.equals(name)) {
                uuid = user.mcAccounts.remove(i);
                found = true;
                break;
            }
        }

        if (update || found) Db.USERS.update(user);
        if (found) {
            event.getChannel().sendMessage(Utils.embed("Unlinked `" + username + "`.").build()).queue();
            if (user.donator) PvpServer.removeDonator(uuid);
        } else {
            event.getChannel().sendMessage(Utils.embed("You don't have linked minecraft account with name `" + username + "`.").build()).queue();
        }
    }
}
