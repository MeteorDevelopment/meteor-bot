package meteordevelopment.meteorbot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public abstract class Command {
    public String name;
    public String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void onMessage(MessageReceivedEvent event);

    public abstract void registerSlashCommand(CommandListUpdateAction commandList);
    public abstract void onSlashCommand(SlashCommandEvent event);
}
