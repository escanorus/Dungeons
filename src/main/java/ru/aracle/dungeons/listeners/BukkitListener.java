package ru.aracle.dungeons.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.destroystokyo.paper.event.player.*;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.entity.EntityDamageItemEvent;
import io.papermc.paper.event.player.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.aracle.dungeons.Dungeons;
import ru.aracle.dungeons.Fractions;
import ru.aracle.dungeons.constructors.Userdata;
import ru.aracle.dungeons.menu.Menu;

public class BukkitListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            if (Dungeons.manager.menu().get(player) != null &&
                    Dungeons.manager.menu().get(player).equals("fractions")) {
                fractions(slot, player); }
        }
    }

    public static void fractions(int slot, Player player) {
        if (slot >= 45 && slot <= 47) {
            player.getInventory().close();
            Fractions.select(player, "warrior");
        }
        if (slot >= 48 && slot <= 50) {
            player.getInventory().close();
            Fractions.select(player, "wizard");
        }
        if (slot >= 51 && slot <= 53) {
            player.getInventory().close();
            Fractions.select(player, "hunter");
        }
    }

    @EventHandler
    public void login(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Userdata userdata = new Userdata(player, Userdata.initialize(player));
        Dungeons.manager.userdata(player, userdata);
        if (userdata.pass().equals("none")) {
            String inform = Dungeons.messages.string("messages.no-pass");
            event.kickMessage(Dungeons.component(inform));
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Dungeons.component(inform));
            return;
        }
        String uuid = Dungeons.manager.userdata().get(player).uuid();
        String fraction = Dungeons.manager.userdata().get(player).fraction();
        Fractions.profile(player, uuid, fraction);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Userdata userdata = Dungeons.manager.userdata().get(player);
        if (userdata.fraction().equals("none")) {
            Menu.open(player, "fractions");
        } else {
            Fractions.inventory(player, false);
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Fractions.inventory(player, true);
        player.getInventory().clear();
        if (Dungeons.manager.menu().get(player) != null) {
            Dungeons.manager.menu().remove(player);
        }
        if (Dungeons.manager.userdata().get(player) != null) {
            Dungeons.manager.userdata().get(player).update();
            Dungeons.manager.userdata().remove(player);
        }
        if (Dungeons.manager.profile().get(player) != null) {
            Dungeons.manager.profile().get(player).update();
            Dungeons.manager.profile().remove(player);
        }
    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void trade(PlayerTradeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String manager = Dungeons.settings.string("permissions.manager");
        String moderator = Dungeons.settings.string("permissions.moderator");
        if (!player.hasPermission(manager) || !player.hasPermission(moderator)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void stand(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void bed(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void item(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void fish(PlayerFishEvent event)  {
        event.setCancelled(true);
    }

    @EventHandler
    public void advancement(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void bucket(PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void bucket(PlayerBucketEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void bucket(PlayerBucketFillEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void book(PlayerEditBookEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void pot(PlayerFlowerPotManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void swing(PlayerArmSwingEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void sleep(PlayerDeepSleepEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void boost(PlayerElytraBoostEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void harvest(PlayerHarvestBlockEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void item(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void item(PlayerItemMendEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void item(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void craft(CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockCookEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockShearEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockDestroyEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(EntityBlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockFertilizeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityBreakDoorEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityChangeBlockEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityBreedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityCombustByBlockEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityCombustByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityDamageByBlockEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityEnterLoveModeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entity(EntityDamageItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void recipe(PlayerRecipeDiscoverEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void name(PlayerNameEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void portal(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void purchase(PlayerPurchaseEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void shear(PlayerShearEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void recipe(PlayerStonecutterRecipeSelectEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void lectern(PlayerTakeLecternBookEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void portal(PlayerTeleportEndGatewayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void shear(PlayerShearBlockEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void close(PlayerInteractEntityEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerInteractEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerInteractAtEntityEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerPickupArrowEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerAttemptPickupItemEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerPickupExperienceEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerSwapHandItemsEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerItemFrameChangeEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerToggleSneakEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerTeleportEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerToggleSprintEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerJumpEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerLaunchProjectileEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void close(PlayerMoveEvent event) { event.setCancelled(Menu.cancel(event.getPlayer())); }

    @EventHandler
    public void entity(EntityDamageEvent event) { if (event.getEntity() instanceof Player) event.setCancelled(Menu.cancel((Player) event.getEntity())); }

    @EventHandler
    public void entity(EntityDamageByEntityEvent event) { if (event.getEntity() instanceof Player) event.setCancelled(Menu.cancel((Player) event.getEntity())); }

}
