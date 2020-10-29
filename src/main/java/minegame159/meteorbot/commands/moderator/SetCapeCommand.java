package minegame159.meteorbot.commands.moderator;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetCapeCommand extends Command {
    public SetCapeCommand() {
        super(Category.Moderator, "Sets pinged person's cape.", "setcape");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        Member member = Utils.onlyModWithPing(event, split);
        if (member == null) return;

        String cape;
        if (split.length > 2) cape = split[2];
        else {
            event.getChannel().sendMessage(Utils.embed("You need to specify the cape name").build()).queue();
            return;
        }

        User user = Db.USERS.get(event.getAuthor());
        if (user == null) {
            user = new User(event.getAuthor());
            Db.USERS.add(user);
        }

        user.cape = cape.equalsIgnoreCase("none") ? "" : cape;
        Db.USERS.update(user);

        event.getChannel().sendMessage(Utils.embed("Changed " + member.getEffectiveName() + " cape to `" + cape + "`.").build()).queue();
    }
}
