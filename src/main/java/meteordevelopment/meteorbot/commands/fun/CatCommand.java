package meteordevelopment.meteorbot.commands.fun;

import kong.unirest.Unirest;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class CatCommand extends Command {
    public CatCommand() {
        super("cat", "Replies with a swag cat.");
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        Unirest.get("https://aws.random.cat/meow").asJsonAsync(response -> {
            event.getMessage().reply(new EmbedBuilder()
                    .setColor(Utils.EMBED_COLOR)
                    .setImage(response.getBody().getObject().getString("file"))
                    .build()
            ).mentionRepliedUser(false).queue();
        });
    }

    @Override
    public void registerSlashCommand(CommandListUpdateAction commandList) {
        commandList.addCommands(new CommandData(name, description)).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Unirest.get("https://aws.random.cat/meow").asJsonAsync(response -> {
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(Utils.EMBED_COLOR)
                    .setImage(response.getBody().getObject().getString("file"))
                    .build()
            ).mentionRepliedUser(false).queue();
        });
    }
}
