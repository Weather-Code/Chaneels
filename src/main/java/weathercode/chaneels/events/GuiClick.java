package weathercode.chaneels.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import weathercode.chaneels.Utils.Util;
import weathercode.chaneels.main.Main;
import weathercode.chaneels.managers.SendServer;

public class GuiClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getView().getTitle().equals(Util.helpcolor("&6&lKANALY"))) {
            e.setCancelled(true);
            if (e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
                Material itemMaterial = e.getClickedInventory().getItem(e.getSlot()).getType();
                if (itemMaterial == Material.MINECART || itemMaterial == Material.CHEST_MINECART) {
                    String itemName = e.getClickedInventory().getItem(e.getSlot()).getItemMeta().getDisplayName();
                    String channelName = Main.getInstance().getConfig().getString("ActualChaneel.ChaneelID");
                    String cleanedItemName = ChatColor.stripColor(itemName);
                    if (cleanedItemName.equals(channelName)) {
                        p.sendMessage(Util.helpcolor("&4Blad: &cJestes juz na tym sektorze"));
                    } else {
                        SendServer.sendtoserver(p, cleanedItemName);
                    }
                }
            }
        }
    }
}
