package minegame159.meteorbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    public final Category category;
    public final String description;
    public final String name;

    public Command(Category category, String description, String name) {
        this.category = category;
        this.description = description;
        this.name = name;
    }

    public abstract void run(MessageReceivedEvent event);
}
