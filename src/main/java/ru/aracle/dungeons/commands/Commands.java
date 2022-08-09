package ru.aracle.dungeons.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.aracle.dungeons.Dungeons;
import ru.aracle.dungeons.Fractions;
import ru.aracle.dungeons.constructors.Item;
import ru.aracle.dungeons.items.Generator;
import ru.aracle.dungeons.menu.Menu;

import java.util.ArrayList;
import java.util.List;

public class Commands implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            player((Player) sender, args);
            return false;
        }
        return false;
    }

    public void player(Player player, String[] args) {
        if (player.hasPermission(Dungeons.settings.string("permissions.manager"))) {
            if (args.length > 0) {
                if (args[0].equals("fractions")) {
                    if (args.length > 1) {
                        if (args[1].equals("drop")) {
                            if (args.length > 2) {
                                if (Dungeons.online().contains(args[2])) {
                                    Fractions.drop(Bukkit.getPlayer(args[2]));
                                    Menu.open(Bukkit.getPlayer(args[2]), "fractions");
                                    player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.command-fractions-drop").replace("%player%", args[2])));
                                } else {
                                    player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.not-online")));
                                }
                            } else {
                                player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.command-usage-fractions-drop")));
                            }
                        }
                        if (args[1].equals("menu")) {
                            Menu.open(player, "fractions");
                        }
                    } else {
                        player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.command-usage-fractions")));
                    }
                }
                if (args[0].equals("items")) {
                    if (args.length > 1) {
                        if (args[1].equals("generate")) {
                            if (args.length > 2) {
                                List<String> items = Dungeons.items.strings("items");
                                if (items.contains(args[2])) {
                                    player.sendMessage(String.valueOf(Generator.generator(args[2])));
                                    int id = Generator.generator(args[2]);
                                    Dungeons.manager.items(id, new Item(id));
                                    player.getInventory().addItem(Dungeons.manager.items().get(id).item());
                                } else {
                                    player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.command-usage-items-generate-not-exists")));
                                }
                            } else {
                                player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.command-usage-items-generate")));
                            }
                        }
                    } else {
                        player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.command-usage-items")));
                    }
                }
                if (args[0].equals("reload")) {
                    Dungeons.settings.reload();
                    Dungeons.messages.reload();
                    Dungeons.items.reload();
                    Dungeons.menu.reload();
                    Dungeons.abilities.reload();
                    player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.configurations-reload")));
                }
            } else {
                player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.command-usage")));
            }
        } else {
            player.sendActionBar(Dungeons.component(Dungeons.messages.string("messages.no-permission")));
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(Dungeons.settings.string("permissions.manager"))) return null;
        List<String> tabs = new ArrayList<>();
        if (args.length <= 1) {
            tabs.add("fractions");
            tabs.add("items");
            tabs.add("reload");
        }
        if (args.length >= 2) {
            if (args.length == 2) {
                if (args[0].equals("fractions")) {
                    tabs.add("drop");
                    tabs.add("menu");
                }
                if (args[0].equals("items")) {
                    tabs.add("generate");
                }
            }
        }
        if (args.length >= 3) {
            if (args.length == 3) {
                if (args[0].equals("fractions")) {
                    if (args[1].equals("drop")) {
                        if (args[2].equals("")) tabs.addAll(Dungeons.online());
                        else {
                            for (String online : Dungeons.online()) {
                                if (online.contains(args[2])) tabs.add(online);
                            }
                        }
                    }
                }
                if (args[0].equals("items")) {
                    if (args[1].equals("generate")) {
                        if (args[2].equals("")) tabs.addAll(Dungeons.items.strings("items"));
                        else {
                            for (String item : Dungeons.items.strings("items")) {
                                if (item.contains(args[2])) tabs.add(item);
                            }
                        }
                    }
                }
            }
        }
        return tabs;
    }
}
