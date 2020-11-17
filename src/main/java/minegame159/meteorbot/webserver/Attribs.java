package minegame159.meteorbot.webserver;

import spark.Request;

public class Attribs {
    public static final Attrib ID = new Attrib("id");

    public static final Attrib REGISTER_ERROR = new Attrib("register-error");

    public static final Attrib LOGIN_REDIRECT = new Attrib("login-redirect");
    public static final Attrib LOGIN_ERROR = new Attrib("login-error");

    public static final Attrib CHANGE_USERNAME_ERROR = new Attrib("change-username-error");

    public static final Attrib CHANGE_PASSWORD_ID = new Attrib("change-password-id");
    public static final Attrib CHANGE_PASSWORD_ERROR = new Attrib("change-password-error");

    public static final Attrib CHANGE_EMAIL_ERROR = new Attrib("change-email-error");

    public static final Attrib FORGOT_PASSWORD_ERROR = new Attrib("forgot-password-error");

    public static final Attrib CUSTOM_CAPE_ERROR = new Attrib("custom-cape-error");

    public static class Attrib {
        private final String name;

        public Attrib(String name) {
            this.name = name;
        }

        public <T> T get(Request request) {
            return request.session().attribute(name);
        }

        public void set(Request request, Object value) {
            request.session().attribute(name, value);
        }

        public <T> T remove(Request request) {
            T value = request.session().attribute(name);
            request.session().removeAttribute(name);
            return value;
        }
    }
}
