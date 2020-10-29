package minegame159.meteorbot;

import minegame159.meteorbot.commands.OkInsultCommand;
import minegame159.meteorbot.commands.OkInsultsCommand;

import java.util.List;

public class OkInsults {
    public static String HELP_STRING;

    private static StringBuilder SB = new StringBuilder();

    public static void init(List<Command> commands) {
        commands.add(add("okamericantrash"));
        commands.add(add("okdude"));
        commands.add(add("okfaggot"));
        commands.add(add("okfurfag"));
        commands.add(add("okhomo"));
        commands.add(add("okjigaboo"));
        commands.add(add("oklibtard"));
        commands.add(add("okmonkey"));
        commands.add(add("oknerd"));
        commands.add(add("oknigga"));
        commands.add(add("oknigger"));
        commands.add(add("okretard"));
        commands.add(add("okspook"));
        commands.add(add("oktranny"));
        commands.add(add("okvirgin"));
        commands.add(add("oksquid"));

        commands.add(new OkInsultsCommand());

        HELP_STRING = SB.toString();
        SB = null;
    }

    private static Command add(String name) {
        if (SB.length() > 0) SB.append(", ");
        SB.append(name);

        return new OkInsultCommand(name);
    }
}
