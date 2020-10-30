package minegame159.meteorbot.commands.normal;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CapeCommand extends Command {
    public CapeCommand() {
        super(Category.Normal, "Displays a preview of a cape.", "cape");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        String name;
        if (split.length > 1) name = split[1];
        else {
            event.getChannel().sendMessage(Utils.embed("Specify a cape name.").build()).queue();
            return;
        }
        if (name.equals("custom")) name = event.getAuthor().getId();

        Cape cape = Db.CAPES.get(name);
        if (cape == null) {
            event.getChannel().sendMessage(Utils.embed("Cape with name `%s` doesn't exist.", name).build()).queue();
            return;
        }

        event.getChannel().sendMessage(cape.url).queue();
    }
}
