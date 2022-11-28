package org.meteordev.meteorbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class InfoChannels extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(MeteorBot.GUILD_ID);
        if (guild == null) {
            MeteorBot.LOG.warn("Failed to fetch guild when initialising info channels.");
            return;
        }

        if (MeteorBot.MEMBER_COUNT_ID == null || MeteorBot.DOWNLOAD_COUNT_ID == null) {
            MeteorBot.LOG.warn("Must define info channel id's for them to function.");
            return;
        }

        VoiceChannel memberCount = guild.getVoiceChannelById(MeteorBot.MEMBER_COUNT_ID);
        VoiceChannel downloads = guild.getVoiceChannelById(MeteorBot.DOWNLOAD_COUNT_ID);
        if (memberCount == null || downloads == null) {
            MeteorBot.LOG.warn("Failed to fetch channels when initialising info channels.");
            return;
        }

        updateChannel(downloads, () -> Utils.apiGet("stats").asJson().getBody().getObject().getLong("downloads"), 6, 0);
        updateChannel(memberCount, () -> (long) MeteorBot.SERVER.findMembers(member -> !member.isPending()).get().size(), 6, 3);
    }

    private static void updateChannel(VoiceChannel channel, Supplier<Long> supplier, int period, int delay) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String name = channel.getName();
            name = name.substring(0, name.lastIndexOf(':') + 1) + " " + Utils.formatLong(supplier.get());
            channel.getManager().setName(name).complete();
        }, delay, period, TimeUnit.MINUTES);
    }
}
