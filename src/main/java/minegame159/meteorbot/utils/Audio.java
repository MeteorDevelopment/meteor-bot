package minegame159.meteorbot.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class Audio {
    public static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    public static void init() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }
}
