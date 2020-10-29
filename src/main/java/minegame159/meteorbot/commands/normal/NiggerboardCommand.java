package minegame159.meteorbot.commands.normal;

import com.mongodb.client.model.Sorts;
import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.database.Db;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;

public class NiggerboardCommand extends Command {
    public NiggerboardCommand() {
        super(Category.Normal, "Display nword leaderboard.", "nwordboard");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        boolean ok = false;

        while (!ok) {
            ok = trySend(event);
        }
    }

    private boolean trySend(MessageReceivedEvent event) {
        EmbedBuilder embed = Utils.embed("");
        int i = 0;

        for (Document document : Db.USERS.getAll().sort(Sorts.descending("nwords")).limit(10)) {
            Member member;
            try {
                member = event.getGuild().retrieveMemberById(document.getString("id")).complete();
            } catch (Exception ignored) {
                return false;
            }

            if (i > 0) embed.appendDescription("\n");
            embed.appendDescription(String.format("**%d.) %s** (Total: %d, Nigger: %d, Nigga: %d)", i + 1, member.getEffectiveName(), document.getInteger("nwords"), document.getInteger("niggerCount"), document.getInteger("niggaCount")));
            i++;
        }

        event.getChannel().sendMessage(embed.build()).queue();
        return true;
    }
}
