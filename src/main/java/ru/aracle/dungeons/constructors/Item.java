package ru.aracle.dungeons.constructors;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.aracle.dungeons.Dungeons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item {

    private int id;
    private String iid;
    private String type;
    private String title;
    private List<String> description;
    private String fraction;
    private String rarity;
    private String abilities;
    private int upgrade;
    private int level;
    private int rang;
    private int cost;
    private int shards;
    private int factor;
    private int speed;
    private int crystals;

    public Item(int id) {
        String query = Dungeons.settings.string("statements.select-items");
        ResultSet result = Dungeons.connection.get(query
                .replace("%condition%", String.valueOf(id)));
        try {
            while (result.next()) {
                this.id = id;
                iid = result.getString("iid");
                type = Dungeons.items.string(iid + ".type");
                title = Dungeons.items.string(iid + ".title");
                description = Dungeons.items.strings(iid + ".description");
                fraction = result.getString("fraction");
                rarity = result.getString("rarity");
                abilities = result.getString("abilities");
                upgrade = result.getInt("upgrade");
                level = result.getInt("level");
                rang = result.getInt("rang");
                cost = result.getInt("cost");
                shards = result.getInt("shards");
                factor = result.getInt("factor");
                speed = result.getInt("speed");
                crystals = result.getInt("crystals");
            }
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
    }

    public void update() {
        Dungeons.connection.set(Dungeons.settings.string("statements.update-items")
                .replace("%level%", String.valueOf(level))
                .replace("%rang%", String.valueOf(rang))
                .replace("%cost%", String.valueOf(cost))
                .replace("%crystals%", String.valueOf(crystals))
                .replace("%shards%", String.valueOf(shards))
                .replace("%upgrade%", String.valueOf(upgrade))
                .replace("%factor%", String.valueOf(factor))
                .replace("%condition%", String.valueOf(id)));
    }

    public ItemStack item() {
        ItemStack item = new ItemStack(material(), 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Dungeons.component(title).decoration(TextDecoration.ITALIC, false));
        meta.lore(lore());
        meta.getPersistentDataContainer().set(new NamespacedKey(Dungeons.instance, "id"), PersistentDataType.INTEGER, id);
        meta.getPersistentDataContainer().set(new NamespacedKey(Dungeons.instance, "factor"), PersistentDataType.INTEGER, factor);
        meta = factor(meta);
        AttributeModifier modifier = new AttributeModifier("speed", speed, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public ItemMeta factor(ItemMeta meta) {
        AttributeModifier modifier = new AttributeModifier("factor", factor, AttributeModifier.Operation.ADD_NUMBER);
        if (type.equals("sword")) meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        return meta;
    }

    public List<String> abilities() {
        List<String> abilities = new ArrayList<>();
        if (this.abilities.contains(",")) abilities = Arrays.asList(this.abilities.split(","));
        else abilities.add(this.abilities);
        return abilities;
    }

    public List<Component> lore() {
        List<Component> lore = new ArrayList<>();
        description.forEach(string -> {
            string = string
                    .replace("%max-upgrade%", Dungeons.settings.string("bounds." + rarity + ".upgrade"))
                    .replace("%level%", String.valueOf(level))
                    .replace("%cost%", String.valueOf(cost))
                    .replace("%crystals%", String.valueOf(crystals))
                    .replace("%shards%", String.valueOf(shards))
                    .replace("%rang%", String.valueOf(rang))
                    .replace("%upgrade%", String.valueOf(upgrade))
                    .replace("%factor%", String.valueOf(factor))
                    .replace("%fraction%",Dungeons.fraction(fraction))
                    .replace("%rarity%",Dungeons.rarity(rarity));
            lore.add(Dungeons.component(string).decoration(TextDecoration.ITALIC, false));
        });
        if (Dungeons.items.statement(iid + ".abilities")) {
            lore.add(Dungeons.component(Dungeons.messages.string("abilities")).decoration(TextDecoration.ITALIC, false));
            abilities().forEach(string -> lore.add(Dungeons.component(string).decoration(TextDecoration.ITALIC, false)));
        }
        return lore;
    }

    public Material material() {
        if (type.equals("sword")) return Material.IRON_SWORD;
        if (type.equals("staff")) return Material.STICK;
        if (type.equals("bow")) return Material.BOW;
        return null;
    }

}
