package meteordevelopment.meteorbot.command.commands;

import kong.unirest.Unirest;
import meteordevelopment.meteorbot.Config;
import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DonatorCommand extends Command {
    public DonatorCommand() {
        super("donator");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("Moderator only command!").queue();
            return;
        }

        event.getMessage().delete().queue();

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

        MeteorBot.GUILD.addRoleToMember(member, MeteorBot.DONATOR_ROLE).queue(unused -> {
            event.getChannel().sendMessage(member.getAsMention() + " thanks for donating to Meteor Client. You can go to https://meteorclient.com and create an account, link your Discord and Minecraft accounts to get a cape. You can also upload a custom one. Also be sure send your Minecraft name so we can give you donator role on our pvp server. (pvp.meteorclient.com)").queue();

            Unirest.post("https://meteorclient.com/api/discord/giveDonator")
                .header("Authorization", Config.BACKEND_TOKEN)
                .queryString("id", member.getId())
                .asEmpty();
        });
    }
}
