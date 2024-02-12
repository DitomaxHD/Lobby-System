package plugin.lobbysystem.util;

import org.bukkit.ChatColor;
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
import plugin.lobbysystem.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Events implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        e.setJoinMessage(null);
        p.setScoreboard(ScoreboardUtils.getScoreboard(p));
        p.getInventory().clear();
        p.getInventory().setItem(4, ServerSelector.getSelector());

        if (Main.instance.getConfig().getLocation("spawn") != null) {
            p.teleport(Main.instance.getConfig().getLocation("spawn"));
        }else {
            p.sendMessage(Main.prefix + "§cThere is no spawn. \n §cPlease informate an admin!");
        }
    }

    @EventHandler
    public void PlayerLeaveEvent(PlayerQuitEvent e) {
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
        if (e.getItem() == null) return;

        if (e.getAction().isRightClick()) {
            if (e.getItem().equals(ServerSelector.getSelector())) {
                e.getPlayer().openInventory(ServerSelector.getInv());
            }
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
