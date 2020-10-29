package minegame159.meteorbot.commands;

import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.commands.Commands;
import minegame159.meteorbot.commands.other.OkInsultCommand;

public class OkInsults {
    public static String HELP_STRING;

    private static StringBuilder SB = new StringBuilder();

    public static void init() {
        Commands.add(add("okamericantrash"));
        Commands.add(add("okdude"));
        Commands.add(add("okfaggot"));
        Commands.add(add("okfurfag"));
        Commands.add(add("okhomo"));
        Commands.add(add("okjigaboo"));
        Commands.add(add("oklibtard"));
        Commands.add(add("okmonkey"));
        Commands.add(add("oknerd"));
        Commands.add(add("oknigga"));
        Commands.add(add("oknigger"));
        Commands.add(add("okretard"));
        Commands.add(add("okspook"));
        Commands.add(add("oktranny"));
        Commands.add(add("okvirgin"));
        Commands.add(add("oksquid"));

        HELP_STRING = SB.toString();
        SB = null;
    }

    private static Command add(String name) {
        if (SB.length() > 0) SB.append(", ");
        SB.append(name);

        return new OkInsultCommand(name);
    }
}
