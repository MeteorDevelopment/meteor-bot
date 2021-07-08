package meteordevelopment.meteorbot.commands.info;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.SimpleCommand;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;

public class BenefitsCommand extends SimpleCommand {
    public BenefitsCommand() {
        super(
            "benefits",
            "Sends information about the benefits of donating to Meteor.",
            Utils.embed("**Donators Receive**:\n• An in-game cape.\n• A colored name and 8 kit slots on Meteor PvP.\n• A role in this server and access to " + MeteorBot.DONATOR_CHAT.getAsMention() + ".").build(),
            new ButtonImpl(null, "Donate", ButtonStyle.LINK, "https://meteorclient.com/donate", false, Emoji.fromEmote(MeteorBot.METEOR))
        );
    }
}
