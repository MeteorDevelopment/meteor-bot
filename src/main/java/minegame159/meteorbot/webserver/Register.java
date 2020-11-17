package minegame159.meteorbot.webserver;

public class Register {
    public final String username;
    public final String email;
    public final String passsword;

    public final long timestamp;

    public Register(String username, String email, String passsword) {
        this.username = username;
        this.email = email;
        this.passsword = passsword;

        this.timestamp = System.currentTimeMillis();
    }
}
