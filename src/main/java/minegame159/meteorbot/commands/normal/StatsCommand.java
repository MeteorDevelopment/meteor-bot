package minegame159.meteorbot.commands.normal;

import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.DailyStats;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.regex.Pattern;

public class StatsCommand extends Command {
    private static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

    public StatsCommand() {
        super(Category.Normal, "Displays today's or specified day's joins and leaves.", "stats");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        String date;
        if (split.length >= 2 && DATE_PATTERN.matcher(split[1]).matches()) date = split[1];
        else date = Utils.getDateString();

        DailyStats dailyStats = Db.DAILY_STATS.get(date);

        if (dailyStats == null) {
            dailyStats = new DailyStats(date, 0, 0);
        }

        event.getChannel().sendMessage(Utils.embed(
                "**Date**: " + dailyStats.date +
                "\n**Joins**: " + dailyStats.joins +
                "\n**Leaves**: " + dailyStats.leaves +
                "\n**Total**: " + dailyStats.getTotalJoins() +
                "\n**Downloads**: " + dailyStats.downloads
        ).build()).queue();
    }
}
