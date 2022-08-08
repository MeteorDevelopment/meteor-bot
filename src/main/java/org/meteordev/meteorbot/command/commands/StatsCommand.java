package org.meteordev.meteorbot.command.commands;

import kong.unirest.json.JSONObject;
import org.meteordev.meteorbot.Utils;
import org.meteordev.meteorbot.command.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class StatsCommand extends Command {
    private static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");

    public StatsCommand() {
        super("stats", "Shows various stats about Meteor.");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data.addOption(OptionType.STRING, "date", "The date to fetch stats for.");
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String date;
        OptionMapping option = event.getOption("date");

        if (option != null && DATE_PATTERN.matcher(option.getAsString()).matches()) {
            date = option.getAsString();
        }
        else {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(new Date());
            date = String.format("%02d-%02d-%d", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        }

        if (date == null) {
            event.reply("Failed to determine date.").setEphemeral(true).queue();
            return;
        }

        JSONObject json = Utils.apiGet("stats", false)
            .queryString("date", date)
            .asJson().getBody().getObject();

        if (json == null || json.has("error")) {
            event.reply("Failed to fetch stats for this date.").setEphemeral(true).queue();
            return;
        }

        int joins = json.getInt("joins"), leaves = json.getInt("leaves");

        event.replyEmbeds(Utils.embed(
            "**Date**: " + json.getString("date") +
                "\n**Joins**: " + joins +
                "\n**Leaves**: " + leaves +
                "\n**Gained**: " + (joins - leaves) +
                "\n**Downloads**: " + json.getInt("downloads")
        ).build()).queue();
    }
}
