package meteordevelopment.meteorbot.commands.fun;

import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.concurrent.ThreadLocalRandom;

public class MonkyCommand extends Command {
    public MonkyCommand() {
        super("monky", "Replies with a monky.");
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        int w = ThreadLocalRandom.current().nextInt(200, 1001);
        int h = ThreadLocalRandom.current().nextInt(200, 1001);

        event.getMessage().reply(new EmbedBuilder()
                .setColor(Utils.EMBED_COLOR)
                .setImage("https://www.placemonkeys.com/" + w + "/" + h + "?random")
                .build()
        ).mentionRepliedUser(false).queue();
    }

    @Override
    public void registerSlashCommand(CommandListUpdateAction commandList) {
        commandList.addCommands(new CommandData(name, description)).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        int w = ThreadLocalRandom.current().nextInt(200, 1001);
        int h = ThreadLocalRandom.current().nextInt(200, 1001);

        event.replyEmbeds(new EmbedBuilder()
                .setColor(Utils.EMBED_COLOR)
                .setImage("https://www.placemonkeys.com/" + w + "/" + h + "?random")
                .build()
        ).mentionRepliedUser(false).queue();
    }
}
