package ru.aracle.dungeons;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration {

    File file;
    FileConfiguration configuration;
    JavaPlugin instance;
    String filename;

    public Configuration(JavaPlugin instance, String filename) {
        this.file = new File(instance.getDataFolder(), filename);
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.instance = instance;
        this.filename = filename;
        create(filename);
    }

    public void create(String filename) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                instance.saveResource(filename, true);
                configuration.load(file);
                Dungeons.info("Configuration file " + filename + " successfully loaded.");
            } catch (IOException | InvalidConfigurationException exception) {
                Dungeons.error(exception.getMessage());
            }
        }
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            Dungeons.error(exception.getMessage());
        }
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public String string(String path) {
        return configuration.getString(path);
    }

    public Integer integer(String path) {
        return configuration.getInt(path);
    }

    public boolean statement(String path) {
        return configuration.getBoolean(path);
    }

    public List<String> strings(String path) {
        return configuration.getStringList(path);
    }

    public void set(String path, Object value) {
        configuration.set(path, value);
    }
}
