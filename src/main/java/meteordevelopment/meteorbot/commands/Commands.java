package meteordevelopment.meteorbot.commands;

import meteordevelopment.meteorbot.MeteorBot;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
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
        // Reset commands
        commands.clear();
        MeteorBot.GUILD.updateCommands().queue();

        // Register commands
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage("meteordevelopment.meteorbot.commands"))
            .setScanners(new SubTypesScanner())
        );

        for (Class<? extends Command> klass : reflections.getSubTypesOf(Command.class)) {
            if (klass.getConstructors()[0].getParameterCount() == 0) {
                try {
                    Command command = klass.newInstance();
                    commands.put(command.name, command);
                    command.registerSlashCommand(MeteorBot.GUILD.updateCommands());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        MeteorBot.LOG.info("Loaded {} commands", commands.size());
    }

    public static void onMessage(MessageReceivedEvent event) {
        String str = event.getMessage().getContentRaw();

        if (str.startsWith(".")) {
            String name = str.substring(1).split(" ")[0];

            Command command = commands.get(name);
            if (command != null) command.onMessage(event);
        }
    }

    public static void onSlashCommand(SlashCommandEvent event) {
        Command command = commands.get(event.getName());
        if (command != null) command.onSlashCommand(event);
    }
}
