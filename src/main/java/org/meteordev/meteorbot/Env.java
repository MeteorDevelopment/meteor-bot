package org.meteordev.meteorbot;

public enum Env {
    DISCORD_TOKEN("DISCORD_TOKEN"),
    API_BASE("API_BASE"),
    BACKEND_TOKEN("BACKEND_TOKEN"),
    GUILD_ID("GUILD_ID"),
    COPE_NN_ID("COPE_NN_ID"),
    MEMBER_COUNT_ID("MEMBER_COUNT_ID"),
    DOWNLOAD_COUNT_ID("DOWNLOAD_COUNT_ID"),
    UPTIME_URL("UPTIME_URL");

    public final String value;

    Env(String key) {
        this.value = System.getenv(key);
    }
}
