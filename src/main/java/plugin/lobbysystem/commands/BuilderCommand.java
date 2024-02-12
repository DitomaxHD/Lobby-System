package plugin.lobbysystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.lobbysystem.Main;
import plugin.lobbysystem.util.ServerSelector;

public class BuilderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p)) return true;
        if (!p.hasPermission("lobby-system.builder")) return true;

        Main main = Main.instance;
        String UUID = p.getUniqueId().toString();

        if (strings.length == 0) {
            if (main.getConfig().getString("builder." + UUID) == null) {
                main.getConfig().set("builder." + UUID, "");
                main.saveConfig();
                p.getInventory().clear();
                p.sendMessage(Main.prefix + "§aYou are now in the builder-mode!");
            }else {
                main.getConfig().set("builder." + UUID, null);
                main.saveConfig();
                p.getInventory().setItem(4, ServerSelector.getSelector());
                p.sendMessage(Main.prefix + "§aYou are not longer in the builder-mode!");
            }
            return true;
        }
        p.sendMessage(Main.prefix + "§7Usage: §c/builder");


        return false;
    }
}
