package minegame159.meteorbot.commands.moderator;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MaxMcAccountsCommand extends Command {
    public MaxMcAccountsCommand() {
        super(Category.Moderator, "Sets max minecraft accounts field for an account.", "maxmcaccounts");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        Member member = Utils.onlyModWithPing(event, split);
        if (member == null) return;

        int maxMcAccounts = -1;
        if (split.length > 2) {
            try {
                int a = Integer.parseInt(split[2]);
                if (a > 0) maxMcAccounts = a;
            } catch (Exception ignored) {}
        }

        if (maxMcAccounts == -1) {
            event.getChannel().sendMessage(Utils.embed("You need to specify a number greater than 0.").build()).queue();
            return;
        }

        User user = Db.USERS.get(member);
        if (user == null) {
            user = new User(member);
            Db.USERS.add(user);
        }

        user.maxMcAccounts = maxMcAccounts;
        Db.USERS.update(user);

        event.getChannel().sendMessage(Utils.embed(member.getEffectiveName() + " can now link " + maxMcAccounts + " minecraft accounts.").build()).queue();
    }
}
