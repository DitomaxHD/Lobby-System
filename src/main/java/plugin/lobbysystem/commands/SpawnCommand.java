package plugin.lobbysystem.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.lobbysystem.Main;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p)) return true;
        if (!p.hasPermission("lobby-system.spawn")) return true;

        if (strings.length == 0) {
            Main main = Main.instance;

            if (main.getConfig().getLocation("spawn") != null) {
                p.teleport(main.getConfig().getLocation("spawn"));
                return true;
            }
            p.sendMessage(Main.prefix + "§cThere is no spawn. \n §cPlease informate an admin!");
            return true;
        }
        p.sendMessage(Main.prefix + "§7Usage: §c/setspawn");
        return false;
    }
}
