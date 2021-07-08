package meteordevelopment.meteorbot.commands.info;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.SimpleCommand;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;

public class FabricCommand extends SimpleCommand {
    public FabricCommand() {
        super(
            "fabric",
            "Sends useful information about the Fabric modloader.",
            Utils.embed("Meteor Client requires the Fabric Loader to run.\nDownload the [Fabric Loader](https://fabricmc.net/use) install using the provided guide.").build(),
            new ButtonImpl(null, "Fabric", ButtonStyle.LINK, "https://fabricmc.net/use", false, Emoji.fromEmote(MeteorBot.FABRIC)),
            new ButtonImpl(null, "Installation Guide", ButtonStyle.LINK, "https://github.com/MeteorDevelopment/meteor-client/wiki/Installation", false, Emoji.fromEmote(MeteorBot.METEOR))
        );
    }
}
