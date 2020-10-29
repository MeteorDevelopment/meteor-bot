package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.PvpServer;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LinkCommand extends Command {
    public LinkCommand() {
        super(Category.Account, "Links a minecraft account to your account.", "link");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        if (split.length < 2) {
            event.getChannel().sendMessage(Utils.embed("Specify your minecraft username.").build()).queue();
            return;
        }

        String username = split[1];
        String uuid = Utils.getMcUuid(username);
        if (uuid == null) {
            event.getChannel().sendMessage(Utils.embed("The username you entered was wrong.").build()).queue();
            return;
        }

        User user = Db.USERS.get(event.getAuthor());
        if (user == null) {
            user = new User(event.getAuthor());
            Db.USERS.add(user);
        }

        boolean ok = false;
        if (user.mcAccounts.size() < user.maxMcAccounts) {
            user.mcAccounts.add(uuid);
            Db.USERS.update(user);
            event.getChannel().sendMessage(Utils.embed("Linked `" + username + "` to your account.").build()).queue();
            ok = true;
        } else if (user.mcAccounts.size() == user.maxMcAccounts && user.maxMcAccounts == 1) {
            user.mcAccounts.set(0, uuid);
            Db.USERS.update(user);
            event.getChannel().sendMessage(Utils.embed("Replaced your previously linked account with `" + username + "`.").build()).queue();
            ok = true;
        } else {
            event.getChannel().sendMessage(Utils.embed("Your %d slots are full, use `.unlink <username>` and `.info` to see your linked minecraft accounts.").build()).queue();
        }

        if (ok && user.donator) PvpServer.giveDonator(uuid);
    }
}
