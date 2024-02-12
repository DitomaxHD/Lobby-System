package plugin.lobbysystem.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServerSelector {

    public static Inventory inv = Bukkit.createInventory(null, 27,"Selector");
    public static ItemStack itemStack = new ItemStack(Material.COMPASS);
    public static ItemMeta itemMeta = itemStack.getItemMeta();
    public static ItemStack getSelector() {
        itemMeta.setDisplayName("§aGamemenu §7(Rightclick)");
        List list = new ArrayList<String>();
        list.add(0, "§7Rightclick, to open the gamemenu!");
        itemMeta.setLore(list);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static Inventory getInv() {
        ItemStack i1 = new ItemStack(Material.WOODEN_AXE);
        ItemMeta m1 = i1.getItemMeta();
        m1.setDisplayName("§a§lGungame");

        List list = new ArrayList<String>();
        list.add(0, "");
        list.add(1, "§7Kill your opponents to get a better weapon!");
        list.add(2, "");
        list.add(3, "§a▶Leftclick to connect");
        list.add(4,"");
        m1.setLore(list);

        m1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        i1.setItemMeta(m1);

        inv.setItem(13, i1);

        return inv;
    }
}
