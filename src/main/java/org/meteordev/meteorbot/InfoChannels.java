package org.meteordev.meteorbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public class InfoChannels extends ListenerAdapter {
    private static final int UPDATE_PERIOD = 6;
    private static int delay;

    @Override
    public void onReady(ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(Env.GUILD_ID.value);
        if (guild == null) {
            MeteorBot.LOG.warn("Failed to fetch guild when initialising info channels.");
            return;
        }

        if (Env.MEMBER_COUNT_ID.value == null || Env.DOWNLOAD_COUNT_ID.value == null) {
            MeteorBot.LOG.warn("Must define info channel id's for them to function.");
            return;
        }

        VoiceChannel memberCount = guild.getVoiceChannelById(Env.MEMBER_COUNT_ID.value);
        VoiceChannel downloads = guild.getVoiceChannelById(Env.DOWNLOAD_COUNT_ID.value);
        if (memberCount == null || downloads == null) {
            MeteorBot.LOG.warn("Failed to fetch channels when initialising info channels.");
            return;
        }

        updateChannel(downloads, () -> Utils.apiGet("stats").asJson().getBody().getObject().getLong("downloads"));
        updateChannel(memberCount, guild::getMemberCount);
    }

    private static void updateChannel(VoiceChannel channel, LongSupplier supplier) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String name = channel.getName();
            name = "%s %s".formatted(name.substring(0, name.lastIndexOf(':') + 1), Utils.formatLong(supplier.getAsLong()));
            channel.getManager().setName(name).complete();
        }, delay, UPDATE_PERIOD, TimeUnit.MINUTES);

        delay += UPDATE_PERIOD / 2;
    }
}
