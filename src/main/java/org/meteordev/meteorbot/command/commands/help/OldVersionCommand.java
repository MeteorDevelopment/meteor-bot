package org.meteordev.meteorbot.command.commands.help;

import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class OldVersionCommand extends HelpCommand {
    public OldVersionCommand() {
        super(
            "old-versions",
            "Tells someone how to play on old versions",
            "Please read the old versions guide before asking more questions.",
            Button.link("https://meteorclient.com/faq/old-versions", "Guide")
        );
    }
}
