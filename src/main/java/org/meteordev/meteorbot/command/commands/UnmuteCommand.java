package org.meteordev.meteorbot.command.commands;

import org.meteordev.meteorbot.MeteorBot;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;
import org.meteordev.meteorbot.command.Commands;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class UnmuteCommand extends Command {
    public UnmuteCommand() {
        super("unmute", "Unmutes the specified member.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data
            .addOption(OptionType.USER, "member", "The member to unmute.", true)
            .addOption(OptionType.STRING, "reason", "The reason for the mute.")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Member member = Commands.parseMember(event);
        if (member == null) return;

        if (!member.isTimedOut()) {
            event.reply("Member isn't already muted.").setEphemeral(true).queue();
            return;
        }

        String reason = "";
        OptionMapping reasonOption = event.getOption("reason");
        if (reasonOption != null) reason = reasonOption.getAsString();

        AuditableRestAction<Void> action = member.removeTimeout();
        if (!reason.isBlank()) action.reason(reason);
        action.complete();

        String message = String.format("**Unmuted %s**", member.getAsMention());
        if (!reason.isBlank()) message += String.format("\nReason: *%s*", reason);
        MessageEmbed embed = Utils.embed(message).build();

        event.replyEmbeds(embed).queue();
        if (!event.getChannel().getId().equals(MeteorBot.MOD_LOG.getId())) {
            MeteorBot.MOD_LOG.sendMessageEmbeds(embed).queue();
        }
    }
}
