package ru.aracle.dungeons;

import net.kyori.adventure.title.Title;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.aracle.dungeons.constructors.Item;
import ru.aracle.dungeons.constructors.Profile;
import ru.aracle.dungeons.constructors.Userdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fractions {

    public static void profile(Player player, String uuid, String fraction) {
        if (fraction.equals("none")) return;
        Profile profile = new Profile(uuid, fraction, Profile.initialize(uuid, fraction));
        Dungeons.manager.profile(player, profile);
    }

    public static void drop(Player player) {
        Userdata userdata = Dungeons.manager.userdata().get(player);
        Profile profile = Dungeons.manager.profile().get(player);
        if (!userdata.fraction().equals("none") && profile != null) {
            inventory(player, true);
            player.getInventory().clear();
            userdata.fraction("none");
            userdata.update();
            profile.update();
            Dungeons.manager.profile().remove(player);
        }
    }

    public static void select(Player player, String fraction) {
        Userdata userdata = Dungeons.manager.userdata().get(player);
        userdata.fraction(fraction);
        String uuid = userdata.uuid();
        Profile profile = new Profile(uuid, fraction, Profile.initialize(uuid, fraction));
        Dungeons.manager.profile(player, profile);
        Dungeons.manager.menu().remove(player);
        inventory(player, false);
        String title = Dungeons.messages.string("messages.choose-fraction-title");
        String subtitle = Dungeons.messages.string("messages.choose-fraction-subtitle");
        player.showTitle(Title.title(Dungeons.component(title), Dungeons.component(subtitle)));
    }

    public static void inventory(Player player, boolean save) {
        if (save) {
            int size = player.getInventory().getSize();
            int slot = 0;
            List<String> items = new ArrayList<>();
            while (slot < size) {
                ItemStack item = player.getInventory().getItem(slot);
                if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Dungeons.instance, "id"), PersistentDataType.INTEGER)) {
                    String id = String.valueOf(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Dungeons.instance, "id"), PersistentDataType.INTEGER));
                    items.add(slot + "_" + id);
                    Dungeons.manager.items().remove(Integer.parseInt(id));
                }
                slot++;
            }
            String inventory = "";
            for (String string : items) {
                inventory = String.format("%s,%s", inventory, string);
            }
            if (items.size() > 0) inventory = inventory.substring(1);
            else inventory = "";
            if (Dungeons.manager.profile().get(player) != null)
            Dungeons.manager.profile().get(player).inventory(inventory);
        } else {
            String inventory = Dungeons.manager.profile().get(player).inventory();
            List<String> items = new ArrayList<>();
            if (inventory.contains(",")) {
                items.addAll(Arrays.asList(inventory.split(",")));
            } else if (inventory.contains("_")) {
                items.add(inventory);
            }
            items.forEach(item -> {
                int slot = Integer.parseInt(item.split("_")[0]);
                int id = Integer.parseInt(item.split("_")[1]);
                Dungeons.manager.items(id, new Item(id));
                player.getInventory().setItem(slot, Dungeons.manager.items().get(id).item());
            });
        }

    }

}
