package org.meteordev.meteorbot.command.commands.help;

import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class OldVersionCommand extends HelpCommand {
    public OldVersionCommand() {
        super(
            "old-versions",
            "tells someone how to play on old versions",
            "We **do not** support old versions of Meteor **or** Minecraft **no matter what!**\nIf you want to play on older servers, use ViaFabric or MultiConnect with the latest version of Meteor.",
            Button.link("https://modrinth.com/mod/multiconnect", "MultiConnect"),
            Button.link("https://modrinth.com/mod/viafabric", "ViaFabric")
        );
    }
}
