package minegame159.meteorbot.commands;

import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.JoinStats;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.regex.Pattern;

public class StatsCommand extends Command {
    private static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

    public StatsCommand() {
        super("stats");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        String date;
        if (split.length >= 2 && DATE_PATTERN.matcher(split[1]).matches()) date = split[1];
        else date = Utils.getDateString();

        JoinStats joinStats = Db.JOIN_STATS.get(date);

        if (joinStats == null) {
            joinStats = new JoinStats(date, 0, 0);
        }

        event.getChannel().sendMessage(Utils.embed(
                "**Date**: " + joinStats.date +
                "\n**Joins**: " + joinStats.joins +
                "\n**Leaves**: " + joinStats.leaves +
                "\n**Total**: " + joinStats.getTotalJoins()
        ).build()).queue();
    }
}
