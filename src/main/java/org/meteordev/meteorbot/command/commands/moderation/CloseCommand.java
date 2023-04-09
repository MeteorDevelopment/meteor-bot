package org.meteordev.meteorbot.command.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

public class CloseCommand extends Command {
    public CloseCommand() {
        super("close", "locks the current forum post");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if (event.getChannelType() != ChannelType.GUILD_PUBLIC_THREAD) {
            event.reply("This command can only be used in a forum post.").setEphemeral(true).queue();
            return;
        }

        if (event.getMember().getId() == event.getChannel().asThreadChannel().getOwnerId()) { 
            event.reply("This post is now locked.").queue(hook -> {
                event.getChannel().asThreadChannel().getManager().setLocked(true).setArchived(true).queue();
            });
            return;
        }

        if (!event.getMember().hasPermission(Permission.MANAGE_THREADS)) {
            event.reply("You don't have permission to lock threads.").setEphemeral(true).queue();
            return;
        }

        event.reply("This post is now locked.").queue(hook -> {
            event.getChannel().asThreadChannel().getManager().setLocked(true).setArchived(true).queue();
        });
    }
}
