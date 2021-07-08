package meteordevelopment.meteorbot.commands.info;

import meteordevelopment.meteorbot.Config;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.SimpleCommand;

public class MulticonnectCommand extends SimpleCommand {
    public MulticonnectCommand() {
        super(
            "multiconnect",
            "Sends useful information about multiconnect.",
            Utils.embed("Use [multiconnect](https://www.curseforge.com/minecraft/mc-mods/multiconnect) to connect to servers that use versions older than " + Config.MC_VERSION + ".").build()
        );
    }
}
