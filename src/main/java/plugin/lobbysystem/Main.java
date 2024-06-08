package plugin.lobbysystem;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.lobbysystem.commands.*;
import plugin.lobbysystem.util.Events;
import plugin.lobbysystem.util.ScoreboardUtils;

public final class Main extends JavaPlugin {

    public static Main instance;
    public static String prefix = "§7[§bSystem§7] ";

    public static LuckPerms luckpermsapi;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("builder").setExecutor(new BuilderCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
        }

        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ScoreboardUtils());
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        saveConfig();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    p.setScoreboard(ScoreboardUtils.getScoreboard(p));
                }
            }
        }.runTaskTimer(this, 20L*5, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        saveConfig();
    }
}
