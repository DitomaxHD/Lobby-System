package plugin.lobbysystem.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.lobbysystem.Main;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p)) return true;
        if (!p.hasPermission("lobby-system.setspawn")) return true;

        if (strings.length == 0) {
            Location ploc = p.getLocation();
            Main main = Main.instance;

            main.getConfig().set("spawn", ploc);
            main.saveConfig();
            p.sendMessage(Main.prefix + "§aYou successfully set a spawn!");
            return true;
        }
        p.sendMessage(Main.prefix + "§7Usage: §c/setspawn");

        return false;
    }
}
