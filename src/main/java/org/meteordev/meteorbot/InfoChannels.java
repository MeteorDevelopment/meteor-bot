package org.meteordev.meteorbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InfoChannels extends ListenerAdapter {
    private static VoiceChannel MEMBER_COUNT, DOWNLOADS;
    private static final long MEMBER_COUNT_ID = 722160636695675034L, DOWNLOADS_ID = 778314030284144661L;

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(MeteorBot.GUILD_ID);
        if (guild == null) {
            MeteorBot.LOG.warn("Failed to fetch guild when initialising info channels.");
            return;
        }

        MEMBER_COUNT = guild.getVoiceChannelById(MEMBER_COUNT_ID);
        DOWNLOADS = guild.getVoiceChannelById(DOWNLOADS_ID);

        if (MEMBER_COUNT == null || DOWNLOADS == null) {
            MeteorBot.LOG.warn("Failed to fetch channels when initialising info channels.");
            return;
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            int downloads = Utils.apiGet("stats", false).asJson().getBody().getObject().getInt("downloads");
            updateChannel(DOWNLOADS, downloads);
        }, 0, 6, TimeUnit.MINUTES);

        executor.scheduleAtFixedRate(() -> {
            updateChannel(MEMBER_COUNT, MeteorBot.GUILD.getMemberCount());
        }, 3, 6, TimeUnit.MINUTES);
    }

    private static void updateChannel(VoiceChannel channel, int value) {
        String name = channel.getName();
        name = name.substring(0, name.lastIndexOf(':') + 1) + " " + value;

        channel.getManager().setName(name).complete();
    }
}
