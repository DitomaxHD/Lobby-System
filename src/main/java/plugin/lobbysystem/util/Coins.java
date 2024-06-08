package plugin.lobbysystem.util;

import org.bukkit.entity.Player;
import plugin.lobbysystem.Main;

public class Coins {

    public static int getCoins(Player p) {
        String UUID = p.getUniqueId().toString();
        Main main = Main.instance;

        if (!main.getConfig().contains("coins." + UUID)) {
            main.getConfig().set("coins." + UUID, 0);
            main.saveConfig();
        }

        return main.getConfig().getInt("coins." + UUID);
    }

    public static void setCoins(Player p, int amount) {
        String UUID = p.getUniqueId().toString();
        Main main = Main.instance;

        main.getConfig().set("coins." + UUID, amount);
        main.saveConfig();
    }

    public static void addCoins(Player p, int amount) {
        String UUID = p.getUniqueId().toString();
        Main main = Main.instance;

        if (!main.getConfig().contains("coins." + UUID)) {
            main.getConfig().set("coins." + UUID, amount);
            main.saveConfig();
        }

        main.getConfig().set("coins." + UUID, getCoins(p) + amount);
        main.saveConfig();
    }

    public static void removeCoins(Player p, int amount) {
        String UUID = p.getUniqueId().toString();
        Main main = Main.instance;

        if (!main.getConfig().contains("coins." + UUID)) {
            main.getConfig().set("coins." + UUID, 0);
            main.saveConfig();
        }else {
            main.getConfig().set("coins." + UUID, getCoins(p) - amount);
            main.saveConfig();
        }
    }
}
