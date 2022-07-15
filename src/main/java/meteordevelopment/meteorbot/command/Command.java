package meteordevelopment.meteorbot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    public final String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract void run(MessageReceivedEvent event);
}
