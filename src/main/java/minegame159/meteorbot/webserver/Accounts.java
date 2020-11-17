package minegame159.meteorbot.webserver;

import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import org.bson.Document;
import spark.Request;

import static com.mongodb.client.model.Filters.eq;

public class Accounts {
    private static final byte[] SEED = new byte[]{1, 5, 9, 6, 9, 32, 8, 6, 2};

    public static String hashPassword(String password) {
        return password;
        //return BCrypt.with(new SecureRandom(SEED)).hashToString(12, password.toCharArray());
    }

    public static Account authenticate(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) return null;

        Account account = getByUsername(login);
        if (account == null) {
            account = getByEmail(login);
            if (account == null) return null;
        }

        return hashPassword(password).equals(account.password) ? account : null;
    }

    public static Account get(Request request) {
        return Db.ACCOUNTS.get(Attribs.ID.<String>get(request));
    }

    public static boolean isEmailValid(String email) {
        return getByEmail(email) == null;
    }

    public static boolean isUsernameValid(String username) {
        return getByUsername(username) == null;
    }

    public static Account getByEmail(String email) {
        Document document = Db.ACCOUNTS.getAll().filter(eq("email", email)).first();
        if (document == null) return null;
        return new Account(document);
    }

    public static Account getByUsername(String username) {
        Document document = Db.ACCOUNTS.getAll().filter(eq("username", username)).first();
        if (document == null) return null;
        return new Account(document);
    }
}
