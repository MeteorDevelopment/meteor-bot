package minegame159.meteorbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    public final String[] names;

    public Command(String... names) {
        this.names = names;
    }

    public abstract void run(MessageReceivedEvent event);

    public boolean isName(String name) {
        for (String n : names) {
            if (name.equals(n)) return true;
        }

        return false;
    }
}
