package meteordevelopment.meteorbot.commands.help;

import meteordevelopment.meteorbot.commands.Category;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CrashLogCommand extends Command {
    public CrashLogCommand() {
        super(Category.Help, "Displays tutorials on how to send crash logs", "crashlog");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(Utils.embed("Please send a crash report or latest.log.\nHere are tutorials on how to do that:\n-[Getting Minecraft Latest Logs](https://minecrafthopper.net/help/guides/getting-minecraft-latest-log/)\n-[How to get a crash report](https://minecraft.fandom.com/wiki/Tutorials/How_to_get_a_crash_report)").build()).queue();
    }
}
