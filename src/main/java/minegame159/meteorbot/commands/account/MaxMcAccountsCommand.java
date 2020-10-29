package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MaxMcAccountsCommand extends Command {
    public MaxMcAccountsCommand() {
        super("maxmcaccounts");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage(Utils.embed("Only for mods ;)").build()).queue();
            return;
        }

        String[] split = event.getMessage().getContentRaw().split(" ");
        Member member = null;

        if (split.length > 1 && split[1].startsWith("<@!") && split[1].endsWith(">")) {
            String id = split[1].substring(3, split[1].length() - 1);
            member = event.getGuild().retrieveMemberById(id).complete();
        }

        if (member == null) {
            event.getChannel().sendMessage(Utils.embed("You need to ping the person.").build()).queue();
            return;
        }

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
