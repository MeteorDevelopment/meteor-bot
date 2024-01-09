package org.meteordev.meteorbot.command.commands.help;

import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class LogsCommand extends HelpCommand {
    public LogsCommand() {
        super(
            "logs",
            "Tells someone how to find their logs",
            "Please read the guide for sending logs before asking more questions.",
            Button.link("https://meteorclient.com/faq/getting-log", "Guide")
        );
    }
}
