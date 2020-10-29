package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import minegame159.meteorbot.database.Db;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class ResetNWordsCommand extends Command {
    public ResetNWordsCommand() {
        super("resetnwords");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        Member member = Utils.onlyModWithPing(event, split);
        if (member == null) return;

        if (Db.USERS.get(member) != null) {
            Db.USERS.update(member, combine(
                    set("niggerCount", 0),
                    set("niggaCount", 0),
                    set("nwords", 0)
            ));

            event.getChannel().sendMessage(Utils.embed(event.getGuild().retrieveMemberById(member.getId()).complete().getEffectiveName() + " enojy being clean ;)").build()).queue();
        }
    }
}
