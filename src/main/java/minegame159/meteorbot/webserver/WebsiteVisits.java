package minegame159.meteorbot.webserver;

import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.DailyStats;
import minegame159.meteorbot.utils.Utils;
import spark.Request;

public class WebsiteVisits {
    private static int websiteVisits;

    private static String lastSaveDate;

    private static long lastSaveTimestamp;
    private static int updateI;

    public static void fetch() {
        DailyStats stats = Db.DAILY_STATS.get(Utils.getDateString());
        websiteVisits = stats.websiteVisits;
    }

    public static void save() {
        save(Utils.getDateString());
    }

    private static void save(String date) {
        if (lastSaveDate == null) lastSaveDate = date;
        else {
            if (!lastSaveDate.equals(date)) {
                save(lastSaveDate);

                fetch();
                return;
            }
        }

        DailyStats stats = Db.DAILY_STATS.get(date);
        stats.websiteVisits = websiteVisits;
        Db.DAILY_STATS.update(stats);

        lastSaveDate = date;
    }

    public static void increment(Request request) {
        Long lastDownloadTime = request.session().attribute("lastWebsiteVisitTime");
        long time = System.currentTimeMillis();

        if (lastDownloadTime == null || time - lastDownloadTime > 60 * 1000) {
            websiteVisits++;

            request.session().attribute("lastWebsiteVisitTime", time);
        }
    }

    public static void update() {
        if (updateI < 10) {
            updateI++;
            return;
        } else updateI = 0;

        long timestamp = System.currentTimeMillis();
        if (lastSaveTimestamp == 0) {
            lastSaveTimestamp = timestamp;
            return;
        }

        int millis = (int) (timestamp - lastSaveTimestamp);
        double seconds = millis / 1000.0;
        double minutes = seconds / 60.0;

        if (minutes > 5) {
            save();
            lastSaveTimestamp = timestamp;
        }
    }

    public static int get() {
        return websiteVisits;
    }
}
