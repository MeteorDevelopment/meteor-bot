package meteordevelopment.meteorbot;

import net.dv8tion.jda.api.entities.VoiceChannel;

public class InfoChannels {
    public static void init() {
        new Thread(InfoChannels::run).start();
    }

    private static void run() {
        long lastTime = 0;

        while (true) {
            long time = System.currentTimeMillis();
            long delta = time - lastTime;

            double minutes = delta / 1000.0 / 60;

            if (minutes >= 10) {
                lastTime = time;

                int downloads = Utils.apiGet("stats", false).asJson().getBody().getObject().getInt("downloads");

                findVoiceChannel("Member Count:").getManager().setName("Member Count: " + MeteorBot.GUILD.getMemberCount()).queue();
                findVoiceChannel("Downloads:").getManager().setName("Downloads: " + downloads).queue();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static VoiceChannel findVoiceChannel(String startsWith) {
        for (VoiceChannel channel : MeteorBot.GUILD.getVoiceChannelCache()) {
            if (channel.getName().startsWith(startsWith)) return channel;
        }

        return null;
    }
}
