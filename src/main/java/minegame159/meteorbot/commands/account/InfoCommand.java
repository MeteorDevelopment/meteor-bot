package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InfoCommand extends Command {
    public InfoCommand() {
        super(Category.Account, "Displays info about your account.", "info");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        String id = event.getAuthor().getId();
        Member member = event.getMember();

        if (split.length > 1) {
            if (split[1].startsWith("<@!") && split[1].endsWith(">")) id = split[1].substring(3, split[1].length() - 1);
            else {
                boolean ok = true;
                try {
                    id = split[1];
                    member = event.getGuild().retrieveMemberById(id).complete();

                    if (member == null) ok = false;
                } catch (Exception ignored) {
                    ok = false;
                }

                if (!ok) {
                    event.getChannel().sendMessage(Utils.embed("Not a valid user id.").build()).queue();
                    return;
                }
            }
        }

        User user = Db.USERS.get(id);
        if (user == null) {
            user = new User(id);
            Db.USERS.add(user);
        }

        StringBuilder sb = new StringBuilder("**Donator:** %s\n**Cape:** %s\n**Has custom cape:** %s");

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

        event.getChannel().sendMessage(Utils.embedTitle("Account info: " + member.getEffectiveName(), sb.toString(), Utils.boolToString(user.donator), user.hasCape() ? user.cape : "none", Utils.boolToString(user.hasCustomCape)).build()).queue();
    }
}
