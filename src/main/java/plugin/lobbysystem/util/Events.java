package plugin.lobbysystem.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import plugin.lobbysystem.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class Events implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        e.setJoinMessage(null);

        if (Main.instance.getConfig().getString("builder." + p.getUniqueId().toString()) != null) {
            Main.instance.getConfig().set("builder." + p.getUniqueId().toString(), null);
            Main.instance.saveConfig();
        }

        p.setScoreboard(ScoreboardUtils.getScoreboard(p));
        p.getInventory().clear();
        p.getInventory().setItem(4, ServerSelector.getSelector());
        p.setGameMode(GameMode.SURVIVAL);

        if (Main.instance.getConfig().getLocation("spawn") != null) {
            p.teleport(Main.instance.getConfig().getLocation("spawn"));
        }else {
            p.sendMessage(Main.prefix + "§cThere is no spawn. \n §cPlease informate an admin!");
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        e.setQuitMessage(null);
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Main main = Main.instance;
        String UUID = e.getPlayer().getUniqueId().toString();

        if (main.getConfig().getString("builder." + UUID) == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Main main = Main.instance;
        String UUID = e.getPlayer().getUniqueId().toString();

        if (main.getConfig().getString("builder." + UUID) == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void WeatherChangeEvent(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        Main main = Main.instance;
        String UUID = e.getPlayer().getUniqueId().toString();

        if (main.getConfig().getString("builder." + UUID) == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
        if (!e.getPlayer().isOp()) {
            if(e.getMessage().toLowerCase().startsWith("/plugins") || e.getMessage().toLowerCase().startsWith("/pl")){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Main main = Main.instance;
        String UUID = e.getPlayer().getUniqueId().toString();

        if (e.getAction().isRightClick()) {
            if (e.getItem() == null) {
                e.setCancelled(true);
                return;
            }

            if (e.getItem().equals(ServerSelector.getSelector())) {
                e.getPlayer().openInventory(ServerSelector.getInv());
            }
        }

        if (main.getConfig().getString("builder." + UUID) == null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        Player p = (Player) e.getWhoClicked();

        Main main = Main.instance;
        String UUID = p.getUniqueId().toString();

        if (main.getConfig().getString("builder." + UUID) == null) {
            e.setCancelled(true);
        }

        if (e.getClickedInventory().equals(ServerSelector.getInv())) {
            if (e.getCurrentItem().getType().equals(Material.WOODEN_AXE)) {
                sendPlayerToServer(p, "gungame");
            }
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getY() <= 0) {
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }

    public static void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(Main.instance, "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            player.sendMessage(ChatColor.RED+"Error when trying to connect to "+server);
        }
    }
}
