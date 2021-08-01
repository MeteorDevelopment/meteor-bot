package minegame159.meteorbot.commands.normal;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import minegame159.meteorbot.commands.Category;
import minegame159.meteorbot.commands.Command;
import minegame159.meteorbot.utils.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.regex.Pattern;

public class StatsCommand extends Command {
    private static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");

    public StatsCommand() {
        super(Category.Normal, "Displays today's or specified day's joins and leaves.", "stats");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");

        String date;
        if (split.length >= 2 && DATE_PATTERN.matcher(split[1]).matches()) date = split[1];
        else date = Utils.getDateString();

        JSONObject json = Unirest.get("https://meteorclient.com/api/stats")
                .queryString("date", date)
                .asJson().getBody().getObject();

        event.getChannel().sendMessage(Utils.embed(
                "**Date**: " + json.getString("date") +
                "\n**Total**: " + (json.getInt("joins") - json.getInt("leaves")) +
                "\n**Joins**: " + json.getInt("joins") +
                "\n**Leaves**: " + json.getInt("leaves") +
                "\n**Downloads**: " + json.getInt("downloads")
        ).build()).queue();
    }
}
