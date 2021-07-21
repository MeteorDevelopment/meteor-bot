package meteordevelopment.meteorbot.commands.fun;

import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.time.Instant;
import java.util.Random;

public class RouletteCommand extends Command {
    private final Random random = new Random();

    public RouletteCommand() {
        super("roulette", "Brave enough?");
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        Member member = event.getMember();

        if (random.nextInt(10) == 0) {
            event.getMessage().reply(new EmbedBuilder()
                .setAuthor(String.format("%s#%s got rouletted", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR)
                .build()
            ).mentionRepliedUser(false).queue();

            member.ban(0, "Ban rouletted").queue();

            // Logging
            EmbedBuilder logEmbed = new EmbedBuilder()
                .setAuthor(String.format("%s#%s was rouletted in #%s", member.getUser().getName(), member.getUser().getDiscriminator(), event.getChannel().getName()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR);

            event.getGuild().getTextChannelById(MeteorBot.LOGS_CHANNEL.getId()).sendMessage(logEmbed.build()).queue();
        } else {
            event.getMessage().reply(new EmbedBuilder()
                .setAuthor(String.format("%s#%s survived a roulette!", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR)
                .build()
            ).mentionRepliedUser(false).queue();
        }
    }

    @Override
    public void registerSlashCommand(CommandListUpdateAction commandList) {
        commandList.addCommands(new CommandData(name, description)).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Member member = event.getMember();

        if (random.nextInt(10) == 0) {
            event.replyEmbeds(new EmbedBuilder()
                .setAuthor(String.format("%s#%s got rouletted", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR)
                .build()
            ).mentionRepliedUser(false).queue();

            member.ban(0, "Ban rouletted").queue();

            // Logging
            EmbedBuilder logEmbed = new EmbedBuilder()
                .setAuthor(String.format("%s#%s was rouletted in #%s", member.getUser().getName(), member.getUser().getDiscriminator(), event.getChannel().getName()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR);

            event.getGuild().getTextChannelById(MeteorBot.LOGS_CHANNEL.getId()).sendMessage(logEmbed.build()).queue();
        } else {
            event.replyEmbeds(new EmbedBuilder()
                .setAuthor(String.format("%s#%s survived a roulette!", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR)
                .build()
            ).mentionRepliedUser(false).queue();
        }
    }
}
