package meteordevelopment.meteorbot.command;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.command.commands.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class Commands {
    private static final Map<String, Command> commands = new HashMap<>();

    public static void init() {
        add(new CatCommand());
        add(new DonatorCommand());
        add(new MonkyCommand());
        add(new StatsCommand());

        MeteorBot.LOG.info("Loaded {} commands", commands.size());
    }

    public static void add(Command command) {
        commands.put(command.name, command);
    }

    public static void onMessage(MessageReceivedEvent event) {
        String str = event.getMessage().getContentRaw();

        if (str.startsWith(".")) {
            String name = str.substring(1).split(" ")[0];

            Command command = commands.get(name);
            if (command != null) command.run(event);
        }
    }
}
