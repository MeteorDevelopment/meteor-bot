package meteordevelopment.meteorbot.commands.info;

import meteordevelopment.meteorbot.Utils;
import meteordevelopment.meteorbot.commands.Command;
import meteordevelopment.meteorbot.database.Database;
import meteordevelopment.meteorbot.database.documents.DbDailyStats;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.regex.Pattern;

public class StatsCommand extends Command {
    private static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");

    public StatsCommand() {
        super("stats", "Sends Meteors discord and downloads stats for the specified date.");
    }

    @Override
    public void onMessage(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        String date;
        if (split.length >= 2 && DATE_PATTERN.matcher(split[1]).matches()) date = split[1];
        else date = Utils.getDateString();

        DbDailyStats dailyStats = Database.DAILY_STATS.get(date);

        if (dailyStats == null) {
            event.getMessage().reply("Couldn't find stats for that date.").mentionRepliedUser(false).queue();
            return;
        }

        event.getMessage().reply(
            Utils.embed(
                "**Date**: " + dailyStats.date +
                    "\n**Total**: " + dailyStats.getTotalJoins() +
                    "\n**Joins**: " + dailyStats.joins +
                    "\n**Leaves**: " + dailyStats.leaves +
                    "\n**Downloads**: " + dailyStats.downloads
            ).build()
        ).mentionRepliedUser(false).queue();
    }

    @Override
    public void registerSlashCommand(CommandListUpdateAction commandList) {
        commandList.addCommands(new CommandData(name, description).addOption(OptionType.STRING, "date", "The date to retrieve stats for.")).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String date = Utils.getDateString();

        if (event.getOption("date") != null) {
            date = event.getOption("date").getAsString();
        }

        if (!DATE_PATTERN.matcher(date).matches()) {
            event.reply("Invalid date input (Format should be DD-MM-YYYY)").mentionRepliedUser(false).queue();
            return;
        }

        DbDailyStats dailyStats = Database.DAILY_STATS.get(date);

        if (dailyStats == null) {
            event.reply("Couldn't find stats for that date.").mentionRepliedUser(false).queue();
            return;
        }

        event.replyEmbeds(
            Utils.embed(
                "**Date**: " + dailyStats.date +
                    "\n**Total**: " + dailyStats.getTotalJoins() +
                    "\n**Joins**: " + dailyStats.joins +
                    "\n**Leaves**: " + dailyStats.leaves +
                    "\n**Downloads**: " + dailyStats.downloads
            ).build()
        ).mentionRepliedUser(false).queue();
    }
}
