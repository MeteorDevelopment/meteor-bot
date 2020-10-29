package minegame159.meteorbot.commands.moderator;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddCape extends Command {
    public AddCape() {
        super(Category.Moderator, "Uploads a new cape.", "addcape");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        if (!Utils.onlyMod(event)) return;
        String[] split = event.getMessage().getContentRaw().split(" ");

        String name;
        if (split.length > 1) name = split[1];
        else {
            event.getChannel().sendMessage(Utils.embed("You need to specify the cape name. (User id if custom cape)").build()).queue();
            return;
        }

        String url;
        if (split.length > 2) url = split[2];
        else {
            event.getChannel().sendMessage(Utils.embed("You need to specify direct url to the image.").build()).queue();
            return;
        }

        Boolean selfAssignable = null;
        if (split.length > 3) {
            if (split[3].equalsIgnoreCase("public")) selfAssignable = true;
        } else selfAssignable = false;
        if (selfAssignable == null) {
            event.getChannel().sendMessage(Utils.embed("Third argument can only be `public`.").build()).queue();
            return;
        }

        Db.CAPES.delete(name);
        Db.CAPES.add(new Cape(name, url, selfAssignable));
        event.getChannel().sendMessage(Utils.embed("Added %s cape `%s`.", selfAssignable ? "public" : "private", name).build()).queue();
    }
}
