package org.meteordev.meteorbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class AutoThreadReply extends ListenerAdapter {


    private final String[][] supportAutoReplyList = {
        {"donutsmp", "donut smp", "hypixel"},
        {"vulcan", "ncp", "aac"},
        {"adfocus", "adfoc.us", "weird site", "cant download", "can't download", "let me download"},
        {"1.19.3", "1.19.2", "1.18.2", "1.17.1", "1.12.2", "1.8.9"},
        {"general chat", "in general", "<#689197706169942135>"},
        {"hurtcam", "hurt cam"},
        {"lunar", "feather", "forge", "quilt", "badlion"},
        {"tlauncher", "t launcher", " tl legacy"}

    };
    private final String[][] supportAutoReplyListIndex = {
        /*
        {"Issue", "Solution"}
        */
        {"Using Meteor on servers that don't allow it", "Do not use Meteor on servers that don't allow it (#info-and-rules Rule 3)"},
        {"Bypassing Anti-Cheats", "Do not use Meteor on servers that don't allow it (#info-and-rules Rule 3)"},
        {"AdFocus when downloading", "Press the 'skip' button in the top right corner - [image](https://cdn.discordapp.com/attachments/959511986587185162/1150379744395800657/image.png)"},
        {"Meteor on unsupported versions", "Use the latest version with the latest version of [VaFabricPlus](<https://github.com/ViaVersion/ViaFabricPlus/releases>)"},
        {"Not able to talk in #general", "Please read the 'Why can’t I talk in the Meteor Discord?' section in the [FAQ](<https://meteorclient.com/faq#why-can%E2%80%99t-i-talk-in-the-meteor-discord?>)"},
        {"NoHurtCam", "NoHurtCam is a vanilla feature. You can enable it under Accessibility Settings > Damage Tilt > 0%"},
        {"Using Meteor on an unsupported launcher/loader", "Meteor/fabric is not supported on your client or mod loader. Please use either a launcher that supports it - we recommend [PrismLauncher](<https://prismlauncher.org/>) - or use fabric"},
        {"Using Meteor on a cracked launcher", "We do not support people who pirate Minecraft. Please buy the game to get support"}
    };

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        if (!event.getChannelType().isThread()) return;
        ThreadChannel thread = event.getChannel().asThreadChannel();
        Message startMessage = thread.retrieveStartMessage().complete();
        String content = startMessage.getContentDisplay();
        content += thread.getName();
        String lContent = content.toLowerCase();
        boolean replyToThread = false;
        String issue = "";
        String solution = "";

        for (int index = 0; index < supportAutoReplyList.length; index++) {
            String[] currentList = supportAutoReplyList[index];
            if (Arrays.stream(currentList).anyMatch(lContent::contains)) {
                String[] issueAndSolution = supportAutoReplyListIndex[index];
                replyToThread = true;
                issue = issueAndSolution[0];
                solution = issueAndSolution[1];
                break;
            }
        }
        if (!replyToThread) return;
        startMessage.reply(String.format("It looks like you are asking about **%s**\n\n%s", issue, solution)).complete();
    }
}
