package minegame159.meteorbot.commands;

import minegame159.meteorbot.MeteorBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;

public class Commands {
    private static final Map<String, Command> commands = new HashMap<>();

    public static void init() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("minegame159.meteorbot.commands"))
                .setScanners(new SubTypesScanner())
        );

        for (Class<? extends Command> klass : reflections.getSubTypesOf(Command.class)) {
            if (klass.getConstructors()[0].getParameterCount() == 0) {
                try {
                    add(klass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        OkInsults.init();

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
