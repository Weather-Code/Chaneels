package weathercode.chaneels.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import weathercode.chaneels.Utils.Util;
import weathercode.chaneels.mysql.mysql;

public class chgui {

    public static void opengui(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, Util.helpcolor("&6&lKANALY"));
        ItemStack yellow = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemStack orange = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        inv.setItem(0, orange);
        inv.setItem(1, yellow);
        inv.setItem(2, white);
        inv.setItem(3, white);
        inv.setItem(4, white);
        inv.setItem(5, white);
        inv.setItem(6, white);
        inv.setItem(7, yellow);
        inv.setItem(8, orange);
        inv.setItem(9, yellow);
        inv.setItem(17, yellow);
        inv.setItem(18, orange);
        inv.setItem(19, yellow);
        inv.setItem(20, white);
        inv.setItem(21, white);
        inv.setItem(22, white);
        inv.setItem(23, white);
        inv.setItem(24, white);
        inv.setItem(25, yellow);
        inv.setItem(26, orange);
        Util.deserializechaneels(mysql.loadchaneels(), inv);
        p.openInventory(inv);
    }
}
