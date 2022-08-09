package ru.aracle.dungeons.menu;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.aracle.dungeons.Dungeons;
import ru.aracle.dungeons.constructors.Userdata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Menu {
    public static void open (Player player, String menu) {
        if (menu.equals("fractions")) fractions(player);
    }

    public static void fractions(Player player) {
        String title = Dungeons.menu.string("fractions.title");
        int size = Dungeons.menu.integer("fractions.size");
        Inventory menu = Bukkit.createInventory(player, size, Dungeons.component(title));
        Dungeons.manager.menu(player, "fractions");
        player.openInventory(menu);
    }

    public static boolean cancel(Player player) {
        Userdata userdata = Dungeons.manager.userdata().get(player);
        String menu = Dungeons.manager.menu().get(player);
        if (userdata.fraction().equals("none") && menu != null && menu.equals("fractions")) {
            open(player, "fractions");
            return true;
        }
        return false;
    }

    public static ItemStack icon(String id) {
        ItemStack icon = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = icon.getItemMeta();
        String title = Dungeons.menu.string(id + ".title");
        meta.displayName(Dungeons.component(title).decoration(TextDecoration.ITALIC, false));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(Dungeons.instance, "icon"), PersistentDataType.STRING, id);
        List<Component> lore = new ArrayList<>();
        List<String> description = Dungeons.menu.strings(id + ".description");
        description.forEach(string -> lore.add(Dungeons.component(string).decoration(TextDecoration.ITALIC, false)));
        WrappedGameProfile profile = new WrappedGameProfile(UUID.randomUUID(), null);
        String texture = Dungeons.menu.string(id + ".texture");
        profile.getProperties().put("textures", new WrappedSignedProperty("textures", texture, texture));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile.getHandle());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
            Dungeons.error(exception.getMessage());
        }
        meta.lore(lore);
        icon.setItemMeta(meta);
        return icon;
    }

}
