package org.meteordev.meteorbot.command.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.meteordev.meteorbot.command.Command;

public class AddonCommand extends Command {
    private static final String message = "You can get addons from https://anticope.ml/addons? \n To install addons you place their jar file in your mods folder alongside meteor client. You can use multiple addons however they may have incompatibilities. \n What does this addon do? Ask the addon's devs not us, we dont make them. Have a problem with the addon? Go ask the addon's devs.";

    public AddonCommand() {
        super("addons", "to install addons");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.USER, "member", "The member to ping.", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Member member = parseMember(event);
        if (member == null) return;

        event.reply("%s %s".formatted(member.getAsMention(), message)).queue();
    }
}
