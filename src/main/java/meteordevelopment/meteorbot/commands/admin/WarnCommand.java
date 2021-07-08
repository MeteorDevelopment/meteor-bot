package meteordevelopment.meteorbot.commands.admin;

import meteordevelopment.meteorbot.MeteorBot;
import meteordevelopment.meteorbot.Mutes;
import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Updates.set;

public class WarnCommand extends Command {
    public WarnCommand() {
        super("warn", "DbMute a user and mutes them progressively.");
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        if (!Utils.isStaff(event.getMember())) {
            event.getMessage().reply(":man_facepalming: smh.").mentionRepliedUser(false).queue();
            return;
        }

        if (event.getMessage().getMentionedMembers(MeteorBot.GUILD).isEmpty() || event.getMessage().getMentionedMembers(MeteorBot.GUILD).size() > 1) {
            event.getMessage().reply("You need to mention someone to warn!").mentionRepliedUser(false).queue();
            return;
        }

        Member member = event.getMessage().getMentionedMembers(MeteorBot.GUILD).get(0);

        int count = Database.MUTES.get(member.getId()) != null ? Database.MUTES.get(member.getId()).count : 1;

        if (count > 4) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(String.format("%s#%s has been banned", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                    .setDescription("**Reason:** Passed warn limit (5)")
                    .setColor(Utils.EMBED_COLOR);

            event.getMessage().reply(embed.build()).mentionRepliedUser(false).queue();
            member.ban(0, "Passed warn limit (5)").queue();
            return;
        }

        String[] split = event.getMessage().getContentRaw().split("<@!" + member.getUser().getId() + ">");

        String reason = "";
        if (split.length > 1) reason = split[1].trim();

        int duration = 86400 * count;

        Mutes.mute(member.getId(), duration, reason);
        Executors.newSingleThreadScheduledExecutor().schedule(() -> Database.MUTES.update(member.getId(), set("count", 0)), 14, TimeUnit.DAYS);

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(String.format("%s#%s has been warned", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR)
                .setFooter("\nMuted until ")
                .setTimestamp(Instant.ofEpochSecond(Instant.now().getEpochSecond() + duration));
        if (!reason.isBlank()) embed.setDescription(String.format("**Reason:** %s", reason));

        event.getMessage().reply(embed.build()).mentionRepliedUser(false).queue();
    }

    @Override
    public void registerSlashCommand(CommandListUpdateAction commandList) {
        commandList.addCommands(new CommandData(name, description)
                .addOption(OptionType.USER, "member", "The member to warn.")
                .addOption(OptionType.STRING, "reason", "The reason for the warn.")
        ).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!Utils.isStaff(event.getMember())) {
            event.reply(":man_facepalming: smh.").mentionRepliedUser(false).queue();
            return;
        }

        Member member = event.getOption("member").getAsMember();

        int count = Database.MUTES.get(member.getId()) != null ? Database.MUTES.get(member.getId()).count : 1;

        if (count > 4) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(String.format("%s#%s has been banned", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                    .setDescription("**Reason:** Passed warn limit (5)")
                    .setColor(Utils.EMBED_COLOR);

            event.replyEmbeds(embed.build()).mentionRepliedUser(false).queue();
            member.ban(0, "Passed warn limit (5)").queue();
            return;
        }

        String reason;
        if (event.getOption("reason") != null) reason = event.getOption("reason").getAsString();
        else reason = "";

        int duration = 86400 * count;

        Mutes.mute(member.getId(), duration, reason);
        Executors.newSingleThreadScheduledExecutor().schedule(() -> Database.MUTES.update(member.getId(), set("count", 0)), 14, TimeUnit.DAYS);

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(String.format("%s#%s has been warned", member.getUser().getName(), member.getUser().getDiscriminator()), null, member.getUser().getEffectiveAvatarUrl())
                .setColor(Utils.EMBED_COLOR)
                .setFooter("\nMuted until ")
                .setTimestamp(Instant.ofEpochSecond(Instant.now().getEpochSecond() + duration));
        if (!reason.isBlank()) embed.setDescription(String.format("**Reason:** %s", reason));

        event.replyEmbeds(embed.build()).mentionRepliedUser(false).queue();
    }
}
