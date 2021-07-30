package minegame159.meteorbot.webserver;

import minegame159.meteorbot.Config;
import minegame159.meteorbot.database.documents.Account;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
    private static final String ADDRESS = "noreply@meteorclient.com";
    private static Session SESSION;

    public static void init() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.zoho.eu");
        props.put("mail.smtp.port", "587");

        SESSION = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ADDRESS, Config.MAIL_PASSWORD);
            }
        });
    }

    public static void sendVerifyEmail(String to, String username, String token) {
        try {
            MimeMessage msg = new MimeMessage(SESSION);
            msg.setFrom(ADDRESS);
            msg.addRecipients(Message.RecipientType.TO, to);
            msg.setSubject("[Meteor] Confirm email");
            msg.setContent("Visit <a href=\"https://meteorclient.com/confirm/" + token + "\" target=\"_blank\">this link</a> to register your account with username <b>" + username + "</b>. Link expires in 10 minutes and can only be used once. https://meteorclient.com/confirm/" + token, "text/html; charset=utf-8");

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendForgotPasswordEmail(Account account, String token) {
        try {
            MimeMessage msg = new MimeMessage(SESSION);
            msg.setFrom(ADDRESS);
            msg.addRecipients(Message.RecipientType.TO, account.email);
            msg.setSubject("[Meteor] Forgot password");
            msg.setContent("Visit <a href=\"https://meteorclient.com/changePassword?token=" + token + "\" target=\"_blank\">this link</a> to change your password. Link expires in 10 minutes and can only be used once. " + "https://meteorclient.com/changePassword?token=" + token, "text/html; charset=utf-8");

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendChangeEmailEmail(Account account, String newEmail, String token) {
        try {
            MimeMessage msg = new MimeMessage(SESSION);
            msg.setFrom(ADDRESS);
            msg.addRecipients(Message.RecipientType.TO, account.email);
            msg.setSubject("[Meteor] Change email");
            msg.setContent("Visit <a href=\"https://meteorclient.com/changeEmail?token=" + token + "\" target=\"_blank\">this link</a> to change your email to <b>" + newEmail + "</b> Make sure that the new email is correct! Link expires in 10 minutes and can only be used once. " + "https://meteorclient.com/changeEmail?token=" + token, "text/html; charset=utf-8");

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
