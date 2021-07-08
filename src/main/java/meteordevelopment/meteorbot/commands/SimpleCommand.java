package meteordevelopment.meteorbot.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class SimpleCommand extends Command {
    private final Component[] components;
    private final MessageEmbed embed;

    public SimpleCommand(String name, String description, MessageEmbed embed, Component... components) {
        super(name, description);

        this.embed = embed;
        this.components = components;
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        if (components.length > 0)
            event.getMessage().reply(embed).setActionRow(components).mentionRepliedUser(false).queue();
        else event.getMessage().reply(embed).mentionRepliedUser(false).queue();
    }

    @Override
    public void registerSlashCommand(CommandListUpdateAction commandList) {
        commandList.addCommands(new CommandData(name, description)).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (components.length > 0) event.replyEmbeds(embed).addActionRow(components).mentionRepliedUser(false).queue();
        else event.replyEmbeds(embed).mentionRepliedUser(false).queue();
    }
}
