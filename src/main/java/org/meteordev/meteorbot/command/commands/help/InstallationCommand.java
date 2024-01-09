package org.meteordev.meteorbot.command.commands.help;

import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class InstallationCommand extends HelpCommand {
    public InstallationCommand() {
        super(
            "installation",
            "Tells someone to read the installation guide",
            "Please read the installation guide before asking more questions.",
            Button.link("https://meteorclient.com/faq/installation", "Guide")
        );
    }
}
