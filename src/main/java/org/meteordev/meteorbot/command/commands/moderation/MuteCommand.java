package org.meteordev.meteorbot.command.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MuteCommand extends Command {
    public MuteCommand() {
        super("mute", "Mutes a user for a maximum of 27 days.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data
            .addOption(OptionType.USER, "member", "The member to mute.", true)
            .addOption(OptionType.STRING, "duration", "The duration of the mute.", true)
            .addOption(OptionType.STRING, "reason", "The reason for the mute.")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Member member = parseMember(event);
        if (member == null) return;

        if (member.isTimedOut()) {
            event.reply("That user is already timed out.").setEphemeral(true).queue();
            return;
        }

        if (member.hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("That member can't be muted.").setEphemeral(true).queue();
            return;
        }

        String duration = event.getOption("duration").getAsString();
        long amount = Utils.parseAmount(duration);
        ChronoUnit unit = Utils.parseUnit(duration);

        if (amount == -1 || unit == null) {
            event.reply("Failed to parse mute duration.").setEphemeral(true).queue();
            return;
        }

        if (unit == ChronoUnit.SECONDS && amount < 10) {
            event.reply("Please input a minimum duration of 10 seconds.").setEphemeral(true).queue();
            return;
        }

        String reason = "";
        OptionMapping reasonOption = event.getOption("reason");
        if (reasonOption != null) reason = reasonOption.getAsString();

        AuditableRestAction<Void> action = member.timeoutFor(Duration.of(amount, unit));
        if (!reason.isBlank()) action.reason(reason);
        action.queue(a -> {
            event.reply("Timed out %s for %s.".formatted(member.getAsMention(), duration)).queue();
        });
    }
}
