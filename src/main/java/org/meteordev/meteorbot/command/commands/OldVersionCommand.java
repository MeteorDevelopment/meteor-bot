package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;


public class OldVersionCommand extends Command {
    public OldVersionCommand() {
        super("old-version", "we don't support old versions");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyEmbeds(Utils.embed("We **DO NOT** support old versions of Meteor/Minecraft no matter what!!\nPlease update to the latest version and use [ViaFabric](https://modrinth.com/mod/viafabric)/[Multiconnect](https://modrinth.com/mod/multiconnect) to connect to older versions if you want any support.").build()).queue();
    }
}
