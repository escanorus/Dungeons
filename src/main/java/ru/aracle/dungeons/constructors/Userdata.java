package ru.aracle.dungeons.constructors;

import org.bukkit.entity.Player;
import ru.aracle.dungeons.Dungeons;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Userdata {

    private String uuid;
    private String username;
    private String pass;
    private String status;
    private String server;
    private String fraction;
    private String clan;

    public String uuid() { return uuid; }
    public String username() { return username; }
    public String pass() { return pass; }
    public String status() { return status; }
    public String server() { return server; }
    public String fraction() { return fraction; }
    public String clan() { return clan; }

    public void uuid(String value) { uuid = value; }
    public void username(String value) { username = value; }
    public void pass(String value) { pass = value; }
    public void status(String value) { status = value; }
    public void server(String value) { server = value; }
    public void fraction(String value) { fraction = value; }
    public void clan(String value) { clan = value; }

    public Userdata(Player player, boolean initialize) {
        String uuid = player.getUniqueId().toString();
        if (initialize) {
            this.uuid = uuid;
            username = player.getName();
            pass = "none";
            status = "none";
            server = Dungeons.settings.string("connection.server");
            fraction = "none";
            clan = "none";
            set();
        }
        get(uuid);
    }

    public void get(String uuid) {
        String query = Dungeons.settings.string("statements.select-userdata");
        ResultSet result = Dungeons.connection.get(query.replace("%condition%", uuid));
        try {
            while (result.next()) {
                this.uuid = uuid;
                username = result.getString("username");
                pass = result.getString("pass");
                status = result.getString("status");
                server = result.getString("server");
                fraction = result.getString("fraction");
                clan = result.getString("clan");
            }
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
    }

    public void update() {
        Dungeons.connection.set(Dungeons.settings.string("statements.update-userdata")
                .replace("%username%", username)
                .replace("%pass%", pass)
                .replace("%status%", status)
                .replace("%server%", server)
                .replace("%fraction%", fraction)
                .replace("%clan%", clan)
                .replace("%condition%", uuid));
    }

    public void set() {
        Dungeons.connection.set(Dungeons.settings.string("statements.insert-userdata")
                .replace("%uuid%", uuid)
                .replace("%username%", username)
                .replace("%pass%", pass)
                .replace("%status%", status)
                .replace("%server%", server)
                .replace("%fraction%", fraction)
                .replace("%clan%", clan));
    }

    public static boolean initialize(Player player) {
        String uuid = player.getUniqueId().toString();
        String query = Dungeons.settings.string("statements.select-userdata");
        ResultSet result = Dungeons.connection.get(query.replace("%condition%", uuid));
        String username = null;
        try {
            while (result.next()) {
                username = result.getString("username");
            }
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
        return username == null;
    }

}
