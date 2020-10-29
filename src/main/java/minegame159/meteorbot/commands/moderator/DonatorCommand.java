package minegame159.meteorbot.commands.moderator;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.PvpServer;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DonatorCommand extends Command {
    public DonatorCommand() {
        super(Category.Moderator, "Set's donator and cape flag for accounts.", "donator");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        Member member = Utils.onlyModWithPing(event, split);
        if (member == null) return;

        boolean bool;
        if (split.length > 2) bool = Utils.stringToBool(split[2]);
        else {
            event.getChannel().sendMessage(Utils.embed("You need to specify `yes` or `no`.").build()).queue();
            return;
        }

        User user = Db.USERS.get(member);
        if (user == null) {
            user = new User(member);
            Db.USERS.add(user);
        }

        if (!user.donator && bool) PvpServer.giveDonatorToAll(user);
        else if (user.donator && !bool) PvpServer.removeDonatorFromAll(user);

        user.donator = bool;
        user.cape = "donator";
        Db.USERS.update(user);

        if (bool) {
            StringBuilder sb = new StringBuilder(member.getEffectiveName());
            sb.append(" now has donator benefits, do `.benefits` do view them.");
            if (user.mcAccounts.isEmpty()) {
                sb.append("\n**IMPORTANT:** You have not linked your minecraft account, do it with `.link <username>`.");
            }

            event.getChannel().sendMessage(Utils.embed(sb.toString()).build()).queue();
        } else {
            event.getChannel().sendMessage(Utils.embed(member.getEffectiveName() + " is not a donator and doesnt have a cape.").build()).queue();
        }
    }
}
