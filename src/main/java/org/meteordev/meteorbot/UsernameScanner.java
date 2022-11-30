package org.meteordev.meteorbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class UsernameScanner extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        checkUser(event.getMember());
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        checkUser(event.getMember());
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        checkUser(event.getMember());
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        checkUser(event.getMember());
    }

    public static void checkUser(Member member) {
        if (member == null) return;

        String name = member.getEffectiveName();
        if (isInvalid(name) && !member.hasPermission(Permission.ADMINISTRATOR)) {
            member.modifyNickname(fixName(name)).complete();
        }
    }

    private static String fixName(String name) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (isValidCharacter(c)) {
                sb.append(c);
            }
        }

        String output = sb.toString().trim();
        return output.isBlank() ? "cope2" : output;
    }

    static boolean isInvalid(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (!isValidCharacter(name.charAt(i))) return true;
        }

        return false;
    }

    private static boolean isValidCharacter(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '"' && c <= '/') || (c >= ':' && c <= '@') || (c >= '[' && c <= '`') || (c >= '{' && c <= '~') || c == ' ' || c == '\t';
    }
}
