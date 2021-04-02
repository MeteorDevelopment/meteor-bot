package minegame159.meteorbot.commands.moderator;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.PvpServer;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.UUID;

public class PvpDonatorCommand extends Command {
    public PvpDonatorCommand() {
        super(Category.Moderator, "Toggles donator status for specified username on pvp server.", "pvpdonator");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        if (!Utils.onlyMod(event)) return;
        String[] split = event.getMessage().getContentRaw().split(" ");

        String username;
        if (split.length > 1) username = split[1];
        else {
            event.getChannel().sendMessage(Utils.embed("You need to specify minecraft username.").build()).queue();
            return;
        }

        UUID uuid = Utils.getMcUuid(username);
        if (uuid == null) {
            event.getChannel().sendMessage(Utils.embed("The username you entered was wrong.").build()).queue();
            return;
        }

        PvpServer.toggleDonator(uuid);
    }
}
