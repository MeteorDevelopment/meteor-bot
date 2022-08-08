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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuteCommand extends Command {
    private static final Pattern TIME_PATTERN = Pattern.compile("^(\\d+)(ms|s|m|h|d|w)$");

    public MuteCommand() {
        super("mute", "Mutes a user for a maximum of 27 days.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data
            .addOption(OptionType.USER, "member", "The member to mute.", true)
            .addOption(OptionType.STRING, "duration", "The duration of the mute.")
            .addOption(OptionType.STRING, "reason", "The reason for the mute.")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Member member = Commands.parseMember(event);
        if (member == null) return;

        if (member.isTimedOut()) {
            event.reply("That user is already timed out.").setEphemeral(true).queue();
            return;
        }

        if (member.hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("That member can't be muted.").setEphemeral(true).queue();
            return;
        }

        long amount;
        ChronoUnit unit;

        OptionMapping durationOption = event.getOption("duration");
        if (durationOption == null) {
            amount = 27;
            unit = ChronoUnit.DAYS;
        }
        else {
            String string = durationOption.getAsString();
            amount = parseAmount(string);
            unit = parseUnit(string);

            if (amount == -1 || unit == null) {
                event.reply("Failed to parse mute duration.").setEphemeral(true).queue();
                return;
            }
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
        action.complete();

        String message = String.format("**Muted %s for %s**", member.getAsMention(), (amount + unitToString(unit)));
        if (!reason.isBlank()) message += String.format("\nReason: *%s*", reason);
        MessageEmbed embed = Utils.embed(message).build();

        event.replyEmbeds(embed).queue();
        if (!event.getChannel().getId().equals(MeteorBot.MOD_LOG.getId())) {
            MeteorBot.MOD_LOG.sendMessageEmbeds(embed).queue();
        }
    }

    public static long parseAmount(String parse) {
        Matcher matcher = TIME_PATTERN.matcher(parse);
        if (!matcher.matches() || matcher.groupCount() != 2) return -1;

        try {
            return Integer.parseInt(matcher.group(1));
        }
        catch (NumberFormatException ignored) {
            return -1;
        }
    }

    public static ChronoUnit parseUnit(String parse) {
        Matcher matcher = TIME_PATTERN.matcher(parse);
        if (!matcher.matches() || matcher.groupCount() != 2) return null;

        return unitFromString(matcher.group(2));
    }

    public static ChronoUnit unitFromString(String string) {
        for (ChronoUnit value : ChronoUnit.values()) {
            String stringValue = unitToString(value);
            if (stringValue == null) continue;

            if (stringValue.equalsIgnoreCase(string)) {
                return value;
            }
        }

        return null;
    }

    public static String unitToString(ChronoUnit unit) {
        return switch (unit) {
            case SECONDS -> "s";
            case MINUTES -> "m";
            case HOURS -> "h";
            case DAYS -> "d";
            case WEEKS -> "w";
            default -> null;
        };
    }
}
