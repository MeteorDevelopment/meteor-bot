package org.meteordev.meteorbot.command.commands;

import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;

public class LinkCommand extends Command {
    public LinkCommand() {
        super("link", "Links your Discord account to your Meteor account.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.STRING, "token", "The token generated on the Meteor website.", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if (!event.getChannel().getType().equals(ChannelType.PRIVATE)) {
            event.reply("This action must be in DMs.").setEphemeral(true).queue();
            return;
        }

        OptionMapping token = event.getOption("token");
        if (token == null || token.getAsString().isBlank()) {
            event.reply("Must specify a valid token.").setEphemeral(true).queue();
            return;
        }

        JSONObject json = Utils.apiPost("account/linkDiscord")
            .queryString("id", event.getUser().getId())
            .queryString("token", token.getAsString())
            .asJson().getBody().getObject();

        if (json.has("error")) {
            event.reply("Failed to link your Discord account. Try generating a new token by refreshing the account page and clicking the link button again.").setEphemeral(true).queue();
            return;
        }

        event.reply("Successfully linked your Discord account.").setEphemeral(true).queue();
    }
}
