package plugin.lobbysystem.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import plugin.lobbysystem.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardUtils implements PluginMessageListener{

    public static String servername;
    public static Scoreboard getScoreboard(Player p) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("test", "dummy");

        Main main = Main.instance;
        String UUID = p.getUniqueId().toString();

        User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(p);
        String userPrefix = "NONE";

        if (user.getCachedData().getMetaData().getPrefix() != null) {
            userPrefix = user.getCachedData().getMetaData().getPrefix().replace("&", "§").replace(" ", "").replace("|", "");
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§b§o■ §8┃ §6§lFoxigames.de §8┃ §b§o■");


        objective.getScore("§7----------------").setScore(12);
        objective.getScore("§6§lName").setScore(11);
        objective.getScore("    §7" + p.getDisplayName()).setScore(10);
        objective.getScore("§a").setScore(9);
        objective.getScore("§6§lRank").setScore(8);

        if (main.getConfig().getString("builder." + UUID) != null) {
            objective.getScore("    §7" + userPrefix + " §7/ §aB").setScore(7);
        }else {
            objective.getScore("    §7" + userPrefix).setScore(7);
        }

        objective.getScore("§b").setScore(6);
        objective.getScore("§6§lCoins").setScore(5);
        objective.getScore("    §7" + Coins.getCoins(p) + "$").setScore(4);
        objective.getScore("§c").setScore(3);
        objective.getScore("§6§lServer").setScore(2);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        Player player = Bukkit.getPlayerExact(p.getName());
        player.sendPluginMessage(main, "BungeeCord", out.toByteArray());

        objective.getScore("    §7" + servername).setScore(1);
        objective.getScore("§a§7----------------").setScore(0);

        return scoreboard;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, @NotNull byte[] bytes) {
        if (!s.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();
        if (subchannel.equals("GetServer")) {
            servername = in.readUTF();
        }
    }
}
