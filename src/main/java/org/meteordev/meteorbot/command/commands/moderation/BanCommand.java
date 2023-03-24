package org.meteordev.meteorbot.command.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

import java.util.concurrent.TimeUnit;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", "bans a member");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data
            .addOption(OptionType.USER, "member", "The member to ban.", true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("You don't have permission to ban members.").setEphemeral(true).queue();
            return;
        }

        Member member = parseMember(event);
        if (member == null) return;

        member.ban(0, TimeUnit.NANOSECONDS).queue(a -> {
            event.reply("Banned %s.".formatted(member.getAsMention())).queue();
        });
    }
}
