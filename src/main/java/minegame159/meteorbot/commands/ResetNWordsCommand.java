package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import minegame159.meteorbot.database.Db;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static com.mongodb.client.model.Updates.*;

public class ResetNWordsCommand extends Command {
    public ResetNWordsCommand() {
        super("resetnwords");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage(Utils.embed("Only for mods ;)").build()).queue();
            return;
        }

        String[] split = event.getMessage().getContentRaw().split(" ");
        if (split.length < 2) {
            event.getChannel().sendMessage(Utils.embed("Specify a user").build()).queue();
            return;
        }

        String id;
        if (split[1].startsWith("<@!") && split[1].endsWith(">")) {
            id = split[1].substring(3, split[1].length() - 1);
        } else {
            event.getChannel().sendMessage(Utils.embed("You need to ping the user").build()).queue();
            return;
        }

        if (Db.USERS.get(id) != null) {
            Db.USERS.update(id, combine(
                    set("niggerCount", 0),
                    set("niggaCount", 0),
                    set("nwords", 0)
            ));

            event.getChannel().sendMessage(Utils.embed(event.getGuild().retrieveMemberById(id).complete().getEffectiveName() + " enojy being clean ;)").build()).queue();
        }
    }
}
