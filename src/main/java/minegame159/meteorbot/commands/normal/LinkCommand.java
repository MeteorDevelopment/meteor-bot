package minegame159.meteorbot.commands.normal;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import minegame159.meteorbot.Config;
import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LinkCommand extends Command {
    public LinkCommand() {
        super(Category.Normal, "Links your Discord account with Meteor account.", "link");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        String token = null;

        if (split.length > 1) {
            token = split[1];
        }

        if (token == null) return;

        Member member;
        try {
            member = event.getGuild().retrieveMemberById(token).complete();
        } catch (Exception ignored) {
            return;
        }

        JSONObject json = Unirest.post("https://meteorclient.com/api/account/linkDiscord")
                .header("Authorization", Config.TOKEN)
                .queryString("id", member.getId())
                .queryString("token", token)
                .asJson().getBody().getObject();

        if (json.has("error")) {
            event.getChannel().sendMessage("Failed to link your Discord account. Try generating a new token by refreshing the account page and clicking the link button again.").queue();
        }
        else {
            event.getChannel().sendMessage("Successfully linked your Discord account.").queue();
        }
    }
}
