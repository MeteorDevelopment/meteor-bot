package meteordevelopment.meteorbot.commands.admin;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.database.Database;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class DonatorCommand extends Command {
    public DonatorCommand() {
        super("donator", "Adds a user as a Meteor donator.");
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        if (!Utils.isDev(event.getMember())) {
            event.getMessage().reply(":man_facepalming: smh.").mentionRepliedUser(false).queue();
            return;
        }

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getMessage().reply("You must mention someone to set as a donator!").mentionRepliedUser(false).queue();
        }

        Member member = event.getMessage().getMentionedMembers().get(0);

        if (Utils.isDonator(member)) {
            event.getGuild().removeRoleFromMember(member, MeteorBot.DONATOR_ROLE).queue(unused -> Database.removeDonor(event.getMember().getId()));
        }
        else {
            event.getGuild().addRoleToMember(member, MeteorBot.DONATOR_ROLE).queue(unused -> {
                MeteorBot.GUILD.getTextChannelById(MeteorBot.DONATOR_CHAT.getId()).sendMessage(member.getAsMention() + " thanks for donating to Meteor " + MeteorBot.UWUCAT.getAsMention() + ". You can go to https://meteorclient.com and create an account, link your Discord and Minecraft accounts to get a cape (and upload your own!).").queue();
                Database.addDonor(event.getMember().getId());
            });
        }
    }

    @Override
    public void registerSlashCommand(CommandListUpdateAction commandList) {
        commandList.addCommands(new CommandData(name, description).addOption(OptionType.USER, "member", "The member to add donator for.", true)).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!Utils.isDev(event.getMember())) {
            event.reply(":man_facepalming: smh.").mentionRepliedUser(false).queue();
            return;
        }

        Member member = event.getOption("member").getAsMember();
        if (member == null) {
            event.reply("I couldn't fetch that member, please try again.").mentionRepliedUser(false).queue();
            return;
        }

        if (Utils.isDonator(member)) {
            event.getGuild().removeRoleFromMember(member, MeteorBot.DONATOR_ROLE).queue(unused -> Database.removeDonor(event.getMember().getId()));
        }
        else {
            event.getGuild().addRoleToMember(member, MeteorBot.DONATOR_ROLE).queue(unused -> {
                MeteorBot.GUILD.getTextChannelById(MeteorBot.DONATOR_CHAT.getId()).sendMessage(member.getAsMention() + " thanks for donating to Meteor " + MeteorBot.UWUCAT.getAsMention() + ". You can go to https://meteorclient.com and create an account, link your Discord and Minecraft accounts to get a cape (and upload your own!).").queue();
                Database.addDonor(event.getMember().getId());
            });

        }

        event.reply("done :)").mentionRepliedUser(false).setEphemeral(true).queue();
    }
}
