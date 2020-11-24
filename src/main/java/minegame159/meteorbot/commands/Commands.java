package minegame159.meteorbot.commands;

import minegame159.meteorbot.MeteorBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands {
    public static String HELP;

    private static final Map<String, Command> commands = new HashMap<>();
    private static final Map<Category, List<Command>> categories = new HashMap<>();

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
        generateHelp();

        MeteorBot.LOG.info("Loaded {} commands", commands.size());
    }

    public static void add(Command command) {
        commands.put(command.name, command);
        categories.computeIfAbsent(command.category, category -> new ArrayList<>()).add(command);
    }

    public static void onMessage(MessageReceivedEvent event) {
        String str = event.getMessage().getContentRaw();

        if (str.startsWith(".")) {
            String name = str.substring(1).split(" ")[0];

            Command command = commands.get(name);
            if (command != null) command.run(event);
        }
    }

    private static void generateHelp() {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (Category category : Category.values()) {
            if (i > 0) sb.append("\n");
            sb.append("\n**").append(category).append(":**");

            List<Command> commands = categories.get(category);
            if (commands != null) {
                for (Command command : commands) {
                    sb.append("\n - *").append(command.name).append(":* ").append(command.description);
                }
            }

            i++;
        }

        HELP = sb.toString();
    }
}
