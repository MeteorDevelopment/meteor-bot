package minegame159.meteorbot.commands;

import minegame159.meteorbot.AirhornAudioSendHandler;
import minegame159.meteorbot.Command;
import minegame159.meteorbot.Utils;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class AirhornCommand extends Command {
    public AirhornCommand() {
        super("airhorn");
    }


    @Override
    public void run(MessageReceivedEvent event) {
        event.getMessage().delete().queue();

        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getChannel().sendMessage(Utils.embed("You need to be in a voice channel").build()).queue();
            return;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel channel = event.getMember().getVoiceState().getChannel();

        if (audioManager.isConnected() && !audioManager.getConnectedChannel().equals(channel)) {
            audioManager.closeAudioConnection();
        }

        audioManager.openAudioConnection(channel);
        audioManager.setSendingHandler(new AirhornAudioSendHandler(audioManager));
    }
}
