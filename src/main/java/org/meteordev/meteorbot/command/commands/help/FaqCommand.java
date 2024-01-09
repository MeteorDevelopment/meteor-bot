package org.meteordev.meteorbot.command.commands.help;

import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class FaqCommand extends HelpCommand {
    public FaqCommand() {
        super(
            "faq",
            "Tells someone to read the faq",
            "The FAQ answers your question, please read it.",
            Button.link("https://meteorclient.com/faq", "FAQ")
        );
    }
}
