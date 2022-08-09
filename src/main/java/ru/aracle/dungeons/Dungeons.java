package ru.aracle.dungeons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.aracle.dungeons.commands.Commands;
import ru.aracle.dungeons.listeners.BukkitListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public final class Dungeons extends JavaPlugin {

    public static Dungeons instance;
    public static Configuration settings;
    public static Configuration messages;
    public static Configuration menu;
    public static Connection connection;
    public static DataManager manager;
    public static Configuration items;
    public static Configuration abilities;

    @Override
    public void onEnable() {
        instance = this;
        settings = new Configuration(instance, "settings.yml");
        messages = new Configuration(instance, "messages.yml");
        menu = new Configuration(instance, "menu.yml");
        items = new Configuration(instance, "items.yml");
        abilities = new Configuration(instance, "abilities.yml");
        connection = new Connection();
        Dungeons.settings.strings("statements.tables").forEach(Dungeons.connection::set);
        manager = new DataManager();
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitListener(), instance);
        Objects.requireNonNull(getCommand("dungeons")).setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.kick(Dungeons.component(Dungeons.messages.string("messages.server-stopped"))));
        if (connection.data != null) connection.close();
    }

    public static List<String> online() {
        List<String> online = new ArrayList<>();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> online.add(player.getName()));
        return online;
    }

    public static @NotNull Component component(String string) {
        return  LegacyComponentSerializer.legacySection().deserialize(string);
    }

    public static String fraction(String fraction) {
        return Dungeons.messages.string("translations.fractions."+fraction);
    }

    public static String rarity(String rarity) {
        return Dungeons.messages.string("translations.rarity." + rarity);
    }

    public static void error(String message) {
        instance.getLogger().log(Level.SEVERE, message);
    }

    public static void info(String message) {
        instance.getLogger().log(Level.INFO, message);
    }

}
