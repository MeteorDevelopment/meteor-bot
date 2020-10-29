package minegame159.meteorbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

public class AirhornAudioSendHandler extends AudioEventAdapter implements AudioSendHandler {
    private final AudioManager audioManager;
    private final AudioPlayer audioPlayer;

    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    public AirhornAudioSendHandler(AudioManager audioManager) {
        this.audioManager = audioManager;
        this.audioPlayer = Audio.MANAGER.createPlayer();

        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);

        audioPlayer.addListener(this);
        Audio.MANAGER.loadItem("https://meteorclient.com/airhorn.ogg", new FunctionalResultHandler(audioPlayer::playTrack, null, null, null));
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        audioManager.closeAudioConnection();
    }

    @Override
    public boolean canProvide() {
        return audioPlayer.provide(frame);
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        buffer.flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
