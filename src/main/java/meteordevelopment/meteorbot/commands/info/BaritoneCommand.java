package meteordevelopment.meteorbot.commands.info;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.SimpleCommand;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;

public class BaritoneCommand extends SimpleCommand {
    public BaritoneCommand() {
        super(
                "baritone",
                "Sends useful information about Baritone.",
                Utils.embed("Meteor Client has Baritone built in.\nTo see Baritone's commands, visit [Baritone's Usage Page](https://github.com/cabaletta/baritone/blob/master/USAGE.md).\nBaritone's default prefix is `#`.").build(),
                new ButtonImpl(null, "GitHub", ButtonStyle.LINK, "https://github.com/cabaletta/baritone", false, Emoji.fromEmote(MeteorBot.GITHUB))
        );
    }
}
