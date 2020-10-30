package minegame159.meteorbot.commands.account;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.database.documents.User;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;

public class CapesCommand extends Command {
    public CapesCommand() {
        super(Category.Account, "Displays list of pre-designed capes you can use.", "capes");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        User user = Db.USERS.get(event.getAuthor());

        StringBuilder sb = new StringBuilder();
        sb.append("**Capes you can use:**");

        sb.append("\n - none");
        if (user != null && user.hasCustomCape) sb.append("\n - custom");

        for (Document document : Db.CAPES.getAll()) {
            Cape cape = new Cape(document);

            if (cape.selfAssignable || Utils.isMod(event.getMember())) sb.append("\n - ").append(cape.name);
        }

        event.getChannel().sendMessage(Utils.embed(sb.toString()).build()).queue();
    }
}
