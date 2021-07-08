package meteordevelopment.meteorbot.commands.info;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.SimpleCommand;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;

public class FpsCommand extends SimpleCommand {
    public FpsCommand() {
        super(
            "fps",
            "Sends information on Fabric Optifine alternatives to boost fps",
            Utils.embed("If you want better performance on Fabric, [Sodium](https://modrinth.com/mod/sodium), [Lithium](https://modrinth.com/mod/lithium), [Phosphor](https://modrinth.com/mod/phosphor) and [Hydrogen](https://modrinth.com/mod/hydrogen) are the best general optimization mods.\nFor more optimization mods, check out [this list](https://gist.github.com/LambdAurora/1f6a4a99af374ce500f250c6b42e8754) of optifine alternatives.").build(),
            new ButtonImpl(null, "Sodium", ButtonStyle.LINK, "https://modrinth.com/mod/sodium", false, Emoji.fromEmote(MeteorBot.MODRINTH)),
            new ButtonImpl(null, "Lithium", ButtonStyle.LINK, "https://modrinth.com/mod/phosphor", false, Emoji.fromEmote(MeteorBot.MODRINTH)),
            new ButtonImpl(null, "Phosphor", ButtonStyle.LINK, "https://modrinth.com/mod/lithium", false, Emoji.fromEmote(MeteorBot.MODRINTH)),
            new ButtonImpl(null, "Hydrogen", ButtonStyle.LINK, "https://modrinth.com/mod/hydrogen", false, Emoji.fromEmote(MeteorBot.MODRINTH)),
            new ButtonImpl(null, "Optifine Alternatives", ButtonStyle.LINK, "https://gist.github.com/LambdAurora/1f6a4a99af374ce500f250c6b42e8754", false, Emoji.fromEmote(MeteorBot.GITHUB))
        );
    }
}
