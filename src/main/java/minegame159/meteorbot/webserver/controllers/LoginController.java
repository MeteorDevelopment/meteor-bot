package minegame159.meteorbot.webserver.controllers;

import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.documents.Account;
import minegame159.meteorbot.utils.Utils;
import minegame159.meteorbot.webserver.*;
import org.apache.velocity.VelocityContext;
import spark.Request;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private static final Map<String, Register> REGISTER_MAP = new HashMap<>();
    public static final Map<String, LoginToken> LOGIN_TOKENS = new HashMap<>();
    private static long lastClearTimestamp;

    public static Route SERVE_REGISTER = (request, response) -> {
        VelocityContext context = new VelocityContext();

        String error = Attribs.REGISTER_ERROR.get(request);
        if (error != null) context.put("error", error);

        return WebServer.render(context, "views/register.html");
    };

    public static Route SERVE_LOGIN = (request, response) -> {
        VelocityContext context = new VelocityContext();

        String error = Attribs.LOGIN_ERROR.remove(request);
        if (error != null) context.put("error", error);

        return WebServer.render(context, "views/login.html");
    };

    public static Route SERVE_CONFIRM = (request, response) -> {
        VelocityContext context = new VelocityContext();

        String account = request.queryParams("account");

        String redirectTo, redirectName;
        if (account == null) {
            redirectTo = "/login";
            redirectName = "Login";
        } else {
            redirectTo = "/account";
            redirectName = "Account";
        }

        context.put("redirectTo", redirectTo);
        context.put("redirectName", redirectName);

        return WebServer.render(context, "views/confirm.html");
    };

    public static Route HANDLE_REGISTER = (request, response) -> {
        String email = request.queryParams("email");
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        if (email == null || username == null || password == null || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Attribs.REGISTER_ERROR.set(request, "One of the fields is empty.");
            response.redirect("/register");
            return null;
        }

        if (!Accounts.isUsernameValid(username)) {
            Attribs.REGISTER_ERROR.set(request, "User with that username already exists.");
            response.redirect("/register");
            return null;
        }

        if (!Accounts.isEmailValid(email)) {
            Attribs.REGISTER_ERROR.set(request, "User with that email already exists.");
            response.redirect("/register");
            return null;
        }

        String token;
        do token = Utils.generateToken();
        while (REGISTER_MAP.containsKey(token));

        REGISTER_MAP.put(token, new Register(username, email, password));
        Mail.sendVerifyEmail(email, username, token);

        Attribs.REGISTER_ERROR.remove(request);
        response.redirect("/confirm");
        return null;
    };

    public static Route HANDLE_CONFIRM = (request, response) -> {
        String token = request.params(":token");

        if (token != null) {
            Register register = REGISTER_MAP.remove(token);

            if (register != null) {
                Db.ACCOUNTS.add(new Account(register));
            }
        }

        response.redirect("/login");
        return null;
    };

    public static Route HANDLE_LOGIN = (request, response) -> {
        Account account = Accounts.authenticate(request.queryParams("login"), request.queryParams("password"));

        if (account != null) {
            Attribs.ID.set(request, account.id);

            String redirect = Attribs.LOGIN_REDIRECT.remove(request);
            if (redirect == null) redirect = "/account";

            Attribs.LOGIN_ERROR.remove(request);
            response.redirect(redirect);
        } else {
            Attribs.LOGIN_ERROR.set(request, "Incorrect username/email or password.");
            response.redirect(request.pathInfo());
        }

        return null;
    };

    public static Route SERVE_FORGOT_PASSWORD = (request, response) -> {
        VelocityContext context = new VelocityContext();

        String error = Attribs.FORGOT_PASSWORD_ERROR.remove(request);
        if (error != null) context.put("error", error);

        return WebServer.render(context, "views/forgotPassword.html");
    };

    public static Route HANDLE_FORGOT_PASSWORD = (request, response) -> {
        String email = request.queryParams("email");

        if (email == null || email.isEmpty()) {
            Attribs.FORGOT_PASSWORD_ERROR.set(request, "Email was empty.");
            response.redirect("/forgotPassword");
            return null;
        }

        Account account = Accounts.getByEmail(email);
        if (account == null) {
            Attribs.FORGOT_PASSWORD_ERROR.set(request, "Account with that email doesn't exist.");
            response.redirect("/forgotPassword");
            return null;
        }

        String token = Utils.generateToken();
        LoginToken login = new LoginToken(account.id);
        LOGIN_TOKENS.put(token, login);
        Mail.sendForgotPasswordEmail(account, token);

        response.redirect("/confirm");
        return null;
    };

    public static Route HANDLE_LOGOUT = (request, response) -> {
        if (!LoginController.isLoggedIn(request)) {
            response.status(401);
            return "";
        }

        Attribs.ID.remove(request);
        return "";
    };

    public static boolean isLoggedIn(Request request) {
        return Attribs.ID.get(request) != null;
    }

    public static Route ensureLoggedIn(Route route, boolean rememberPage) {
        return (request, response) -> {
            if (Attribs.ID.get(request) == null) {
                if (rememberPage) Attribs.LOGIN_REDIRECT.set(request, request.pathInfo());
                else Attribs.LOGIN_REDIRECT.remove(request);

                response.redirect("/login");
                return null;
            }

            return route.handle(request, response);
        };
    }

    public static Route ensureLoggedIn(Route route) {
        return ensureLoggedIn(route, true);
    }

    public static void onRequest() {
        long timestamp = System.currentTimeMillis();
        if (lastClearTimestamp == 0) {
            lastClearTimestamp = timestamp;
            return;
        }

        int millis = (int) (timestamp - lastClearTimestamp);
        double seconds = millis / 1000.0;
        double minutes = seconds / 60.0;

        if (minutes > 1.0) {
            clearOldTokens();
            lastClearTimestamp = timestamp;
        }
    }

    private static void clearOldTokens() {
        long timestamp = System.currentTimeMillis();

        REGISTER_MAP.keySet().removeIf(token -> {
            Register register = REGISTER_MAP.get(token);

            int millis = (int) (timestamp - register.timestamp);
            double seconds = millis / 1000.0;
            double minutes = seconds / 60.0;

            return minutes > 10.0;
        });

        LOGIN_TOKENS.keySet().removeIf(token -> {
            LoginToken login = LOGIN_TOKENS.get(token);

            int millis = (int) (timestamp - login.timestamp);
            double seconds = millis / 1000.0;
            double minutes = seconds / 60.0;

            return minutes > 10.0;
        });

        AccountController.CHANGE_EMAIL_TOKENS.keySet().removeIf(token -> {
            AccountController.ChangeEmail login = AccountController.CHANGE_EMAIL_TOKENS.get(token);

            int millis = (int) (timestamp - login.timestamp);
            double seconds = millis / 1000.0;
            double minutes = seconds / 60.0;

            return minutes > 10.0;
        });
    }

    public static class LoginToken {
        public final String id;
        private final long timestamp;

        public LoginToken(String id) {
            this.id = id;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
