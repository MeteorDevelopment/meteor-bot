package meteordevelopment.meteorbot.command.commands;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import meteordevelopment.meteorbot.command.Command;
import meteordevelopment.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class StatsCommand extends Command {
    private static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");

    public StatsCommand() {
        super("stats");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        String date;
        if (split.length >= 2 && DATE_PATTERN.matcher(split[1]).matches()) date = split[1];
        else {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(new Date());
            date = String.format("%02d-%02d-%d", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        }

        JSONObject json = Unirest.get("https://meteorclient.com/api/stats")
                .queryString("date", date)
                .asJson().getBody().getObject();

        int joins = json.getInt("joins"), leaves = json.getInt("leaves");

        event.getChannel().sendMessage(Utils.embed(
                "**Date**: " + json.getString("date") +
                "\n**Joins**: " + joins +
                "\n**Leaves**: " + leaves +
                "\n**Gained**: " + (joins - leaves) +
                "\n**Downloads**: " + json.getInt("downloads")
        ).build()).queue();
    }
}
