package ru.aracle.dungeons.items;

import ru.aracle.dungeons.Dungeons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class Generator {

    public static int generator(String iid) {
        String fraction = Dungeons.items.string(iid + ".fraction");
        String rarity = Dungeons.items.string(iid + ".rarity");
        rarity = rarity(rarity);
        fraction = fraction(fraction);
        return generate(iid, fraction, rarity);
    }

    public static int generate(String iid, String fraction, String rarity) {
        Random random = new Random();
        String abilities = "";
        List<String> list = Dungeons.abilities.strings(rarity);
        int upgrade = 1;
        int level = 1;
        int rang = 1;
        if (rarity.equals("common") || rarity.equals("rare")) {
            if (rarity.equals("rare")) {
                level = random.nextInt(10);
                rang = random.nextInt(10);
            }
            int index = random.nextInt(list.size());
            abilities = list.get(index);
        }
        if (rarity.equals("epic") || rarity.equals("legendary")) {
            if (rarity.equals("epic")) {
                level = random.nextInt(20 - 10) + 10;
                rang = random.nextInt(20 - 10) + 10;
            }
            if (rarity.equals("legendary")) {
                level = random.nextInt(30 - 20) + 20;
                rang = random.nextInt(30 - 20) + 20;
            }
            int first = random.nextInt(list.size());
            int second = first;
            while (second == first) { second = random.nextInt(list.size()); }
            abilities = list.get(first)+","+list.get(second);
        }
        if (rarity.equals("relic")) {
            level = random.nextInt(40 - 30) + 30;
            rang = random.nextInt(40 - 30) + 30;
            int first = random.nextInt(list.size());
            int second = first;
            while (second == first) { second = random.nextInt(list.size()); }
            int third = second;
            while (third == second || third == first) { third = random.nextInt(list.size()); }
            abilities = list.get(first)+","+list.get(second)+","+list.get(third);
        }
        int minCost = Dungeons.settings.integer("bounds" + rarity + ".min-cost");
        int maxCost = Dungeons.settings.integer("bounds." + rarity + ".max-cost");
        int minShards = Dungeons.settings.integer("bounds." + rarity + ".min-shards");
        int maxShards = Dungeons.settings.integer("bounds." + rarity + ".max-shards");
        int minDamage = Dungeons.settings.integer("bounds." + rarity + ".min-damage");
        int maxDamage = Dungeons.settings.integer("bounds." + rarity + ".max-damage");
        int minCrystals = Dungeons.settings.integer("bounds." + rarity + ".min-crystals");
        int maxCrystals = Dungeons.settings.integer("bounds." + rarity + ".max-crystals");
        int factor = random.nextInt(maxDamage-minDamage) + minDamage;
        int speed = Dungeons.settings.integer("bounds." + rarity + ".speed");
        int cost = random.nextInt((maxCost + factor) - (minCost + factor)) + (minCost + factor);
        int shards = random.nextInt((maxShards + factor) - (minShards + factor)) + (minShards + factor);
        int crystals = random.nextInt((maxCrystals + factor) - (minCrystals + factor)) + (minCrystals + factor);
        int id = last() + 1;
        Dungeons.connection.set(Dungeons.settings.string("statements.insert-items")
                .replace("%id%", String.valueOf(id))
                .replace("%iid%", iid)
                .replace("%fraction%", fraction)
                .replace("%rarity%", rarity)
                .replace("%abilities%", abilities)
                .replace("%upgrade%", String.valueOf(upgrade))
                .replace("%level%", String.valueOf(level))
                .replace("%rang%", String.valueOf(rang))
                .replace("%cost%", String.valueOf(cost))
                .replace("%shards%", String.valueOf(shards))
                .replace("%factor%", String.valueOf(factor))
                .replace("%speed%", String.valueOf(speed))
                .replace("%crystals%", String.valueOf(crystals)));
        return id;
    }

    public static int last() {
        int last = 0;
        ResultSet result = Dungeons.connection.get(Dungeons.settings.string("statements.select-id-items"));
        try {
            while (result.next()) {
                last = result.getInt("id");
            }
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
        return last;
    }

    public static String rarity(String rarity) {
        Random random = new Random();
        if (rarity.equals("random")) {
            int index = random.nextInt(10000);
            if (index < 9500) rarity = "common";
            if (index >= 9000 && index < 9500) rarity = "rare";
            if (index >= 9500 && index < 9800) rarity = "epic";
            if (index >= 9800 && index < 9950) rarity = "legendary";
            if (index >= 9950) rarity = "relic";
            if (!rarity.equals("common")) {
                index = random.nextInt(2);
                if (index == 1) rarity = "common";
            }
        }
        return rarity;
    }

    public static String fraction(String fraction) {
        Random random = new Random();
        if (fraction.equals("fraction")) {
            List<String> fractions = Dungeons.settings.strings("fractions");
            int index = random.nextInt(fractions.size());
            fraction = fractions.get(index);
        }
        return fraction;
    }
}
