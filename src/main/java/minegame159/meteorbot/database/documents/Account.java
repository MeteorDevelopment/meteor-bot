package minegame159.meteorbot.database.documents;

import minegame159.meteorbot.MeteorBot;
import minegame159.meteorbot.database.Db;
import minegame159.meteorbot.database.ISerializable;
import minegame159.meteorbot.webserver.Accounts;
import minegame159.meteorbot.webserver.Register;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Account implements ISerializable {
    public String id;
    public String email;
    public String username;
    public String password;

    public boolean donator;

    public String discordId;

    public int maxMcAccounts;
    public List<String> mcAccounts;

    public String cape;
    public boolean canHaveCustomCape;

    public Account(Document document) {
        id = document.getString("id");
        email = document.getString("email");
        username = document.getString("username");
        password = document.getString("password");

        donator = document.getBoolean("donator");

        discordId = document.getString("discordId");

        maxMcAccounts = document.getInteger("maxMcAccounts");
        mcAccounts = document.getList("mcAccounts", String.class);

        cape = document.getString("cape");
        canHaveCustomCape = document.getBoolean("canHaveCustomCape");
    }

    public Account(Register register) {
        Stats stats = Db.GLOBAL.get(Stats.class, Stats.ID);

        this.id = Integer.toString(stats.totalAccounts);
        this.email = register.email;
        this.username = register.username;
        this.password = Accounts.hashPassword(register.passsword);

        this.donator = false;

        this.discordId = "";

        this.maxMcAccounts = 1;
        this.mcAccounts = new ArrayList<>(0);

        this.cape = "";
        this.canHaveCustomCape = false;

        stats.totalAccounts++;
        Db.GLOBAL.update(stats);
    }

    public boolean isAdmin() {
        if (discordId.isEmpty()) return false;

        Guild guild = MeteorBot.JDA.getGuildById("689197705683140636");
        Member member = guild.retrieveMemberById(discordId).complete();

        return member.hasPermission(Permission.ADMINISTRATOR) || member.getRoles().contains(guild.getRoleById("689197893340758022"));
    }

    public boolean hasCape() {
        return !cape.isEmpty();
    }

    public void giveDonator() {
        giveDonatorRole();

        if (!donator) {
            donator = true;
            canHaveCustomCape = true;
            cape = "donator";
        }
    }

    public void giveDonatorRole() {
        if (donator && !discordId.isEmpty()) {
            Guild guild = MeteorBot.JDA.getGuildById("689197705683140636");
            guild.addRoleToMember(discordId, guild.getRoleById("689205464574984353")).queue();
        }
    }

    @Override
    public Document serialize() {
        return new Document()
                .append("id", id)
                .append("email", email)
                .append("username", username)
                .append("password", password)

                .append("donator", donator)

                .append("discordId", discordId)

                .append("maxMcAccounts", maxMcAccounts)
                .append("mcAccounts", mcAccounts)

                .append("cape", cape)
                .append("canHaveCustomCape", canHaveCustomCape);
    }

    @Override
    public String getId() {
        return id;
    }
}
