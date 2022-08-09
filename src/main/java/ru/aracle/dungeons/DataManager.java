package ru.aracle.dungeons;

import org.bukkit.entity.Player;
import ru.aracle.dungeons.constructors.Item;
import ru.aracle.dungeons.constructors.Profile;
import ru.aracle.dungeons.constructors.Userdata;

import java.util.HashMap;

public class DataManager {

    private final HashMap<Integer, Item> items = new HashMap<>();
    public HashMap<Integer, Item> items() { return items; }
    public void items(Integer id, Item item) { items().put(id, item); }

    private final HashMap<Player, String> menu = new HashMap<>();
    public HashMap<Player, String> menu() { return menu; }
    public void menu(Player player, String menu) { menu().put(player, menu); }

    private final HashMap<Player, Userdata> userdata = new HashMap<>();
    public HashMap<Player, Userdata> userdata() { return userdata; }
    public void userdata(Player player, Userdata userdata) { userdata().put(player, userdata); }

    private final HashMap<Player, Profile> profile = new HashMap<>();
    public HashMap<Player, Profile> profile() { return profile; }
    public void profile(Player player, Profile profile) { profile().put(player, profile); }

}
