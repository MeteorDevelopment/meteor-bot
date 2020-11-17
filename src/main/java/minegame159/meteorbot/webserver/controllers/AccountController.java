package minegame159.meteorbot.webserver.controllers;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import minegame159.meteorbot.Config;
import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import minegame159.meteorbot.database.documents.Cape;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.webserver.Accounts;
import minegame159.meteorbot.webserver.Attribs;
import minegame159.meteorbot.webserver.Mail;
import minegame159.meteorbot.webserver.WebServer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.apache.velocity.VelocityContext;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountController {
    public static final Map<String, ChangeEmail> CHANGE_EMAIL_TOKENS = new HashMap<>();

    public static Route SERVE_ACCOUNT = LoginController.ensureLoggedIn((request, response) -> {
        VelocityContext context = new VelocityContext();

        Account account = Accounts.get(request);

        context.put("username", account.username);
        context.put("donator", account.donator);
        context.put("canHaveCustomCape", account.canHaveCustomCape);
        context.put("maxMcAccounts", account.maxMcAccounts);

        if (!account.discordId.isEmpty()) {
            User user = MeteorBot.JDA.retrieveUserById(account.discordId).complete();

            context.put("discordName", user.getName());
            context.put("discordNumber", user.getDiscriminator());
            context.put("discordAvatar", user.getEffectiveAvatarUrl());
        }

        List<McAccount> mcAccounts = new ArrayList<>();
        for (String uuid : account.mcAccounts) mcAccounts.add(new McAccount(Utils.getMcUsername(uuid), uuid));
        context.put("mcAccounts", mcAccounts);

        List<Cape> capes = new ArrayList<>();
        capes.add(new Cape("", "").set(account, "None"));
        if (account.donator) capes.add(Db.CAPES.get("donator").set(account, null));
        if (account.isAdmin()) capes.add(Db.CAPES.get("moderator").set(account, null));
        if (account.canHaveCustomCape) {
            Cape customCape = Db.CAPES.get("account_" + account.id);
            if (customCape != null) capes.add(customCape.set(account, "Custom"));
        }

        String customCapeError = Attribs.CUSTOM_CAPE_ERROR.remove(request);
        if (customCapeError != null) context.put("customCapeError", customCapeError);

        context.put("capes", capes);

        return WebServer.render(context, "views/account.html");
    });

    public static Route HANDLE_SELECT_CAPE = (request, response) -> {
        String capeName = request.queryParams("cape");

        if (capeName == null) return "";

        if (!LoginController.isLoggedIn(request)) {
            response.status(401);
            return "";
        }

        Cape cape = Db.CAPES.get(capeName);

        if (cape != null || capeName.isEmpty()) {
            Account account = Accounts.get(request);
            boolean ok = false;

            if (capeName.isEmpty()) ok = true;
            else if (capeName.equals("donator") && account.donator) ok = true;
            else if (capeName.equals("moderator") && account.isAdmin()) ok = true;
            else if (capeName.startsWith("account_") && account.canHaveCustomCape) ok = true;

            if (ok) {
                account.cape = capeName;
                Db.ACCOUNTS.update(account);
            }
        }

        return "";
    };

    public static Route SERVE_CHANGE_USERNAME = LoginController.ensureLoggedIn((request, response) -> {
        VelocityContext context = new VelocityContext();

        String error = Attribs.CHANGE_USERNAME_ERROR.get(request);
        if (error != null) context.put("error", error);

        return WebServer.render(context, "views/changeUsername.html");
    });

    public static Route HANDLE_CHANGE_USERNAME = (request, response) -> {
        String newUsername = request.queryParams("username");

        if (newUsername == null || newUsername.isEmpty()) {
            Attribs.CHANGE_USERNAME_ERROR.set(request, "Username was empty.");
            response.redirect("/changeUsername");
            return null;
        }

        if (!LoginController.isLoggedIn(request)) {
            response.redirect("/login");
            return null;
        }

        if (!Accounts.isUsernameValid(newUsername)) {
            Attribs.CHANGE_USERNAME_ERROR.set(request, "Username is taken.");
            response.redirect("/changeUsername");
            return null;
        }

        Account account = Accounts.get(request);
        account.username = newUsername;
        Db.ACCOUNTS.update(account);

        response.redirect("/account");
        return null;
    };

    public static Route SERVE_CHANGE_PASSWORD = (request, response) -> {
        String token = request.queryParams("token");
        LoginController.LoginToken login = LoginController.LOGIN_TOKENS.remove(token);

        if (login == null && !LoginController.isLoggedIn(request)) {
            response.redirect("/login");
            return null;
        }

        VelocityContext context = new VelocityContext();

        if (login != null) {
            Attribs.CHANGE_PASSWORD_ID.set(request, login.id);
        } else {
            context.put("requiresOldPassword", true);
            Attribs.CHANGE_PASSWORD_ID.remove(request);
        }

        String error = Attribs.CHANGE_PASSWORD_ERROR.remove(request);
        if (error != null) context.put("error", error);

        return WebServer.render(context, "views/changePassword.html");
    };

    public static Route HANDLE_CHANGE_PASSWORD = (request, response) -> {
        String oldPassword = request.queryParams("old");
        String newPassword = request.queryParams("new");

        String id = Attribs.CHANGE_PASSWORD_ID.remove(request);

        if (newPassword == null || newPassword.isEmpty() || (id == null && (oldPassword == null || oldPassword.isEmpty()))) {
            Attribs.CHANGE_PASSWORD_ERROR.set(request, "Passwords were empty.");
            response.redirect("/changePassword");
            return null;
        }

        if (id == null && !LoginController.isLoggedIn(request)) {
            response.redirect("/login");
            return null;
        }

        Account account = id == null ? Accounts.get(request) : Db.ACCOUNTS.get(id);

        if (id == null && !account.password.equals(oldPassword)) {
            Attribs.CHANGE_PASSWORD_ERROR.set(request, "Passwords did not match.");
            response.redirect("/changePassword");
            return null;
        }

        account.password = newPassword;
        Db.ACCOUNTS.update(account);

        response.redirect("/account");
        return null;
    };

    public static Route SERVE_CHANGE_EMAIL = LoginController.ensureLoggedIn((request, response) -> {
        String token = request.queryParams("token");
        ChangeEmail changeEmail = CHANGE_EMAIL_TOKENS.remove(token);

        if (token == null) {
            VelocityContext context = new VelocityContext();

            String error = Attribs.CHANGE_EMAIL_ERROR.remove(request);
            if (error != null) context.put("error", error);

            return WebServer.render(context, "views/changeEmail.html");
        }

        Account account = Db.ACCOUNTS.get(changeEmail.id);
        account.email = changeEmail.newEmail;
        Db.ACCOUNTS.update(account);

        response.redirect("/account");
        return null;
    });

    public static Route HANDLE_CHANGE_EMAIL = (request, response) -> {
        String newEmail = request.queryParams("email");

        if (newEmail == null || newEmail.isEmpty()) {
            Attribs.CHANGE_EMAIL_ERROR.set(request, "Email was empty.");
            response.redirect("/changeEmail");
            return null;
        }

        if (!LoginController.isLoggedIn(request)) {
            response.redirect("/login");
            return null;
        }

        if (!Accounts.isEmailValid(newEmail)) {
            Attribs.CHANGE_EMAIL_ERROR.set(request, "Email is taken.");
            response.redirect("/changeEmail");
            return null;
        }

        Account account = Accounts.get(request);
        String token = Utils.generateToken();
        CHANGE_EMAIL_TOKENS.put(token, new ChangeEmail(account.id, newEmail));
        Mail.sendChangeEmailEmail(account, newEmail, token);

        response.redirect("/confirm?account");
        return null;
    };

    public static Route HANDLE_DISCORD_AUTH = LoginController.ensureLoggedIn((request, response) -> {
        String accessCode = request.queryParams("code");

        if (accessCode != null) {
            // Get access token
            HttpResponse<JsonNode> res = Unirest.post("https://discord.com/api/oauth2/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("client_id", "742092137218179172")
                    .field("client_secret", Config.DISCORD_SECRET)
                    .field("grant_type", "authorization_code")
                    .field("code", accessCode)
                    .field("redirect_uri", "https://meteorclient.com/discordAuth")
                    .field("scope", "identify guilds.join")
                    .asJson();

            String accessToken = res.getBody().getObject().getString("access_token");

            // Get user id
            res = Unirest.get("https://discord.com/api/users/@me")
                    .header("Authorization", "Bearer " + accessToken)
                    .asJson();

            String userId = res.getBody().getObject().getString("id");

            // Join user to meteor discord
            Unirest.put("https://discord.com/api/guilds/689197705683140636/members/" + userId)
                    .header("Authorization", "Bot " + Config.DISCORD_TOKEN)
                    .header("Content-Type", "application/json")
                    .body(new JSONObject().put("access_token", accessToken))
                    .asJson();

            Guild guild = MeteorBot.JDA.getGuildById("689197705683140636");
            guild.addRoleToMember(userId, guild.getRoleById("777248653445300264")).queue();

            Account account = Accounts.get(request);
            account.discordId = userId;
            Db.ACCOUNTS.update(account);

            response.redirect("/account");
        } else {
            response.redirect("https://discord.com/api/oauth2/authorize?client_id=742092137218179172&redirect_uri=https%3A%2F%2Fmeteorclient.com%2FdiscordAuth&response_type=code&scope=identify%20guilds.join");
        }

        return null;
    });

    public static Route HANDLE_UNLINK_DISCORD = (request, response) -> {
        if (!LoginController.isLoggedIn(request)) {
            response.status(401);
            return "";
        }

        Account account = Accounts.get(request);

        Guild guild = MeteorBot.JDA.getGuildById("689197705683140636");
        guild.removeRoleFromMember(account.discordId, guild.getRoleById("777248653445300264")).queue();

        account.discordId = "";
        Db.ACCOUNTS.update(account);

        return "";
    };

    public static Route HANDLE_ADD_MC_ACCOUNT = LoginController.ensureLoggedIn((request, response) -> {
        Account account = Accounts.get(request);
        String username = request.queryParams("username");

        if (account.mcAccounts.size() < account.maxMcAccounts && username != null && !username.isEmpty()) {
            String uuid = Utils.getMcUuid(username);

            if (uuid != null) {
                account.mcAccounts.add(uuid);
                Db.ACCOUNTS.update(account);
            }
        }

        response.redirect("/account");
        return null;
    }, false);

    public static Route HANDLE_REMOVE_MC_ACCOUNT = (request, response) -> {
        if (!LoginController.isLoggedIn(request)) {
            response.status(401);
            return "";
        }

        String uuid = request.queryParams("uuid");

        if (uuid != null) {
            Account account = Accounts.get(request);
            account.mcAccounts.removeIf(s -> s.equals(uuid));
            Db.ACCOUNTS.update(account);
        }

        return "";
    };

    public static class McAccount {
        private final String name;
        private final String uuid;

        public McAccount(String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public String getUuid() {
            return uuid;
        }
    }

    public static class ChangeEmail {
        public final String id;
        public final String newEmail;

        public final long timestamp;

        public ChangeEmail(String id, String newEmail) {
            this.id = id;
            this.newEmail = newEmail;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
