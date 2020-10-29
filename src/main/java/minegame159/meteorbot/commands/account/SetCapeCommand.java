package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;

public class SetCapeCommand extends Command {
    public SetCapeCommand() {
        super(Category.Account, "Sets pinged person's cape.", "setcape");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        Member member = null;
        if (split.length > 1 && split[1].startsWith("<@!") && split[1].endsWith(">")) {
            String id = split[1].substring(3, split[1].length() - 1);
            member = event.getGuild().retrieveMemberById(id).complete();

            if (!Utils.isMod(event.getMember())) {
                event.getChannel().sendMessage(Utils.embed("Only mods can change other account's capes.").build()).queue();
                return;
            }
        }

        String cape;
        if (split.length > (member == null ? 1 : 2)) cape = split[member == null ? 1 : 2];
        else {
            event.getChannel().sendMessage(Utils.embed("You need to specify the cape name. Use `.capes` to view which capes you can use.").build()).queue();
            return;
        }

        User user = Db.USERS.get(member == null ? event.getAuthor() : member);
        if (user == null) {
            user = new User(member == null ? event.getAuthor() : member);
            Db.USERS.add(user);
        }

        boolean ok1 = false;
        boolean ok2 = false;
        for (Document document : Db.CAPES.getAll()) {
            Cape c = new Cape(document);

            if (!Utils.isMod(event.getMember()) && !c.selfAssignable) continue;

            if (cape.equals(c.name)) ok1 = true;
        }
        if (cape.equals("custom") && (user.hasCustomCape || Utils.isMod(event.getMember()))) ok2 = true;
        if (!ok1 && !ok2) {
            event.getChannel().sendMessage(Utils.embed("Wrong cape. Use `.capes` to view which capes you can use.").build()).queue();
            return;
        }

        user.cape = cape.equalsIgnoreCase("none") ? "" : cape;
        if (cape.equals("custom")) user.hasCustomCape = true;
        Db.USERS.update(user);

        event.getChannel().sendMessage(Utils.embed("Changed " + (member == null ? event.getMember() : member).getEffectiveName() + " cape to `" + cape + "`.").build()).queue();
    }
}
