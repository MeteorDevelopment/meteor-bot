package minegame159.meteorbot.commands.moderator;

import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class DonatorCommand extends Command {
    public DonatorCommand() {
        super(Category.Moderator, "Pinged person gets donator.", "donator");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        if (!Utils.onlyMod(event)) return;

        String[] split = event.getMessage().getContentRaw().split(" ");
        String id = null;

        if (split.length > 1) {
            id = split[1];
        }

        if (id == null) return;

        Member member;
        try {
            member = event.getGuild().retrieveMemberById(id).complete();
        } catch (Exception ignored) {
            return;
        }

        event.getGuild().addRoleToMember(member, MeteorBot.DONATOR_ROLE).queue(unused -> {
            event.getChannel().sendMessage(member.getAsMention() + " thanks for donating to Meteor Client. You can go to https://meteorclient.com and create an account, link your Discord and Minecraft accounts to get a cape. You can also upload a custom one. Also be sure send your Minecraft name so we can give you donator role on our pvp server. (pvp.meteorclient.com)").queue();
        });

        Account account = Db.ACCOUNTS.get(eq("discordId", id));

        if (account != null) {
            Db.ACCOUNTS.update(account.id, combine(
                    set("donator", true),
                    set("canHaveCustomCape", true),
                    set("cape", "donator")
            ));
        }
    }
}
