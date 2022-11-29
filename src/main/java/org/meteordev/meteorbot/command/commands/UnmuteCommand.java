package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

public class UnmuteCommand extends Command {
    public UnmuteCommand() {
        super("unmute", "Unmutes the specified member.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.USER, "member", "The member to unmute.", true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Member member = parseMember(event);
        if (member == null) return;

        if (!member.isTimedOut()) {
            event.reply("Member isn't already muted.").setEphemeral(true).queue();
            return;
        }

        member.removeTimeout().queue(a -> {
            event.reply("Removed timeout for %s.".formatted(member.getAsMention())).queue();
        });
    }
}
