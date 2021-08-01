package meteordevelopment.meteorbot;

import kong.unirest.Unirest;
import meteordevelopment.meteorbot.utils.Utils;

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

            if (minutes >= 3) {
                lastTime = time;

                int downloads = Unirest.get("https://meteorclient.com/api/stats").asJson().getBody().getObject().getInt("downloads");

                Utils.findVoiceChannel(MeteorBot.GUILD, "Member Count:").getManager().setName("Member Count: " + MeteorBot.GUILD.getMemberCount()).queue();
                Utils.findVoiceChannel(MeteorBot.GUILD, "Downloads:").getManager().setName("Downloads: " + downloads).queue();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
