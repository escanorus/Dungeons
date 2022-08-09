package ru.aracle.dungeons.constructors;

import org.bukkit.entity.Player;
import ru.aracle.dungeons.Dungeons;
import ru.aracle.dungeons.items.Generator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Profile {

    private String uuid;
    private String fraction;
    private String inventory;
    private String location;
    private int level;
    private int rang;
    private int gold;
    private int crystals;
    private int shards;
    private int arcana;
    private int endurance;
    private int kills;
    private int deaths;
    private int wins;
    private int losses;

    public String uuid() { return uuid; }
    public String fraction() { return fraction; }
    public String inventory() { return inventory; }
    public String location() { return location; }
    public int level() { return level; }
    public int rang() { return rang; }
    public int gold() { return gold; }
    public int crystals() { return crystals; }
    public int shards() { return shards; }
    public int arcana() { return arcana; }
    public int endurance() { return endurance; }
    public int kills() { return kills; }
    public int deaths() { return deaths; }
    public int wins() { return wins; }
    public int losses() { return losses; }

    public void uuid(String value) { uuid = value; }
    public void fraction(String value) { fraction = value; }
    public void inventory(String value) { inventory = value; }
    public void location(String value) { location = value; }
    public void level(int value) { level = value; }
    public void rang(int value) { rang = value; }
    public void gold(int value) { gold = value; }
    public void crystals(int value) { crystals = value; }
    public void shards(int value) { shards = value; }
    public void arcana(int value) { arcana = value; }
    public void endurance(int value) { endurance = value; }
    public void kills(int value) { kills = value; }
    public void deaths(int value) { deaths = value; }
    public void wins(int value) { wins = value; }
    public void losses(int value) { losses = value; }

    public Profile(String uuid, String fraction, boolean initialize) {
        if (initialize) {
            this.uuid = uuid;
            this.fraction = fraction;
            if (fraction.equals("warrior")) inventory = "0_" + Generator.generator("warrior-sword");
            if (fraction.equals("wizard")) inventory = "0_" + Generator.generator("wizard-staff");
            if (fraction.equals("hunter")) inventory = "0_" + Generator.generator("hunter-bow");
            location = "";
            level = 1;
            rang = 1;
            gold = 50;
            crystals = 10;
            shards = 50;
            arcana = 20;
            endurance = 20;
            kills = 0;
            deaths = 0;
            wins = 0;
            losses = 0;
            set();
        }
        get(uuid, fraction);
    }

    public void update() {
        Dungeons.connection.set(Dungeons.settings.string("statements.update-profile")
                .replace("%inventory%", inventory)
                .replace("%location%", location)
                .replace("%level%", String.valueOf(level))
                .replace("%rang%", String.valueOf(rang))
                .replace("%gold%", String.valueOf(gold))
                .replace("%crystals%", String.valueOf(crystals))
                .replace("%shards%", String.valueOf(shards))
                .replace("%arcana%", String.valueOf(arcana))
                .replace("%endurance%", String.valueOf(endurance))
                .replace("%kills%", String.valueOf(kills))
                .replace("%deaths%", String.valueOf(deaths))
                .replace("%wins%", String.valueOf(wins))
                .replace("%losses%", String.valueOf(losses))
                .replace("%uuid-condition%", uuid)
                .replace("%fraction-condition%", fraction));
    }

    public void get(String uuid, String fraction) {
        String query = Dungeons.settings.string("statements.select-profile");
        ResultSet result = Dungeons.connection.get(query
                .replace("%uuid-condition%", uuid)
                .replace("%fraction-condition%", fraction));
        try {
            while (result.next()) {
                this.uuid = uuid;
                this.fraction = fraction;
                inventory = result.getString("inventory");
                location = result.getString("location");
                level = result.getInt("level");
                rang = result.getInt("rang");
                gold = result.getInt("gold");
                crystals = result.getInt("crystals");
                shards = result.getInt("shards");
                arcana = result.getInt("arcana");
                endurance = result.getInt("endurance");
                kills = result.getInt("kills");
                deaths = result.getInt("deaths");
                wins = result.getInt("wins");
                losses = result.getInt("losses");
            }
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
    }

    public void set() {
        Dungeons.connection.set(Dungeons.settings.string("statements.insert-profile")
                .replace("%uuid%", uuid)
                .replace("%fraction%", fraction)
                .replace("%inventory%", inventory)
                .replace("%location%", location)
                .replace("%level%", String.valueOf(level))
                .replace("%rang%", String.valueOf(rang))
                .replace("%gold%", String.valueOf(gold))
                .replace("%crystals%", String.valueOf(crystals))
                .replace("%shards%", String.valueOf(shards))
                .replace("%arcana%", String.valueOf(arcana))
                .replace("%endurance%", String.valueOf(endurance))
                .replace("%kills%", String.valueOf(kills))
                .replace("%deaths%", String.valueOf(deaths))
                .replace("%wins%", String.valueOf(wins))
                .replace("%losses%", String.valueOf(losses)));
    }

    public static boolean initialize(String uuid, String fraction) {
        String query = Dungeons.settings.string("statements.select-profile");
        ResultSet result = Dungeons.connection.get(query
                .replace("%uuid-condition%", uuid)
                .replace("%fraction-condition%", fraction));
        String inventory = null;
        try {
            while (result.next()) {
                inventory = result.getString("inventory");
            }
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
        return inventory == null;
    }

}
