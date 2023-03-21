package org.meteordev.meteorbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class InfoChannels extends ListenerAdapter {
    private static final String GUILD_ID = System.getenv("GUILD_ID");
    private static final String MEMBER_COUNT_ID = System.getenv("MEMBER_COUNT_ID");
    private static final String DOWNLOAD_COUNT_ID = System.getenv("DOWNLOAD_COUNT_ID");

    private static final int UPDATE_PERIOD = 6;
    private static int delay;

    @Override
    public void onReady(ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(GUILD_ID);
        if (guild == null) {
            MeteorBot.LOG.warn("Failed to fetch guild when initialising info channels.");
            return;
        }

        if (MEMBER_COUNT_ID == null || DOWNLOAD_COUNT_ID == null) {
            MeteorBot.LOG.warn("Must define info channel id's for them to function.");
            return;
        }

        VoiceChannel memberCount = guild.getVoiceChannelById(MEMBER_COUNT_ID);
        VoiceChannel downloads = guild.getVoiceChannelById(DOWNLOAD_COUNT_ID);
        if (memberCount == null || downloads == null) {
            MeteorBot.LOG.warn("Failed to fetch channels when initialising info channels.");
            return;
        }

        updateChannel(downloads, () -> Utils.apiGet("stats").asJson().getBody().getObject().getLong("downloads"));
        updateChannel(memberCount, () -> (long) MeteorBot.SERVER.getMemberCount());
    }

    private static void updateChannel(VoiceChannel channel, Supplier<Long> supplier) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String name = channel.getName();
            name = name.substring(0, name.lastIndexOf(':') + 1) + " " + Utils.formatLong(supplier.get());
            channel.getManager().setName(name).complete();
        }, delay, UPDATE_PERIOD, TimeUnit.MINUTES);

        delay += UPDATE_PERIOD / 2;
    }
}
