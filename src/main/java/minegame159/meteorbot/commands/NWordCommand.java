package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NWordCommand extends Command {
    public NWordCommand() {
        super("nword");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        String name;
        User user;

        if (split.length == 1) {
            name = "You";
            user = Db.USERS.get(event.getAuthor());
        } else if (split.length == 2 && split[1].startsWith("<@!") && split[1].endsWith(">")) {
            String id = split[1].substring(3, split[1].length() - 1);
            Member member = event.getGuild().retrieveMemberById(id).complete();
            if (member == null) return;

            name = member.getEffectiveName();
            user = Db.USERS.get(id);
        } else return;

        EmbedBuilder embed;
        if (user == null || user.nwords == 0) {
            embed = Utils.embed("%s are clean", name);
        } else {
            embed = Utils.embed("%s said `nigger` %d times and `nigga` %d times", name, user.niggerCount, user.niggaCount);
        }
        event.getChannel().sendMessage(embed.build()).queue();
    }
}
