package weathercode.chaneels.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import weathercode.chaneels.Utils.EnderChest;
import weathercode.chaneels.Utils.Util;
import weathercode.chaneels.main.Main;
import weathercode.chaneels.mysql.mysql;

import java.util.List;

public class JoinQuitEvent implements Listener {
    @EventHandler
    public void Join(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String thischaneel = Main.getInstance().getConfig().getString("ActualChaneel.ChaneelID");
        if(mysql.isuseradded(p.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Location loc = new Location(p.getWorld(), mysql.getX(p.getUniqueId()), mysql.getY(p.getUniqueId()), mysql.getZ(p.getUniqueId()));
                String inv = mysql.getInv(p.getUniqueId());
                String enderchest = mysql.getEnderChest(p.getUniqueId());
                Util.deserializeDirection(p, loc, mysql.getplayerdirection(p.getUniqueId()));
                p.getInventory().setContents(Util.deserializeInventory(mysql.getInv(p.getUniqueId())));
                p.getInventory().setArmorContents(Util.deserializeArmor(mysql.getArmor(p.getUniqueId())));
                GameMode gm = mysql.getgm(p.getUniqueId());
                p.setGameMode(gm);
                p.setHealth(mysql.gethealth(p.getUniqueId()));
                p.getInventory().setHeldItemSlot(mysql.getslot(p.getUniqueId()));
                p.setFoodLevel(mysql.getfeed(p.getUniqueId()));
                Util.deserializeEffects(p, mysql.geteffects(p.getUniqueId()));
                Util.deserializeexp(p, mysql.getexp(p.getUniqueId()));
                p.getEnderChest().setContents(EnderChest.deserializeEc(mysql.getEnderChest(p.getUniqueId())));
            }, 15);
        } else {
            mysql.adduser(p.getUniqueId(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(),Util.serializeInventory(p.getInventory().getContents()), Util.serializeArmor(p.getInventory().getArmorContents()) ,thischaneel, "");
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                p.kickPlayer(Util.helpcolor("&aPomyslnie dodano cie do bazy danych"));
            }, 10);
        }
    }
    @EventHandler
    public void Quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        mysql.setgm(p.getUniqueId(), p.getGameMode());
        Player player = e.getPlayer();
        double playerx = p.getLocation().getX();
        double playery = p.getLocation().getY();
        double playerz = p.getLocation().getZ();
        mysql.setX(p.getUniqueId(), playerx);
        mysql.setZ(p.getUniqueId(), playerz);
        mysql.setY(p.getUniqueId(), playery);
        mysql.setY(p.getUniqueId(), p.getLocation().getY());
        mysql.setinv(p.getUniqueId(), Util.serializeInventory(p.getInventory().getContents()));
        mysql.setArmor(p.getUniqueId(), Util.serializeArmor(p.getInventory().getArmorContents()));
        Main.getInstance().getLogger().info(Util.serializeInventory(p.getInventory().getContents()));
        mysql.sethealth(p.getUniqueId(), p.getHealth());
        mysql.setslot(p.getUniqueId(), p.getInventory().getHeldItemSlot());
        mysql.setfeed(p.getUniqueId(), p.getFoodLevel());
        mysql.seteffects(p.getUniqueId(), Util.serializeEffects(p));
        mysql.setplayerdirection(p.getUniqueId(), Util.serializeDirection(p));
        mysql.setexp(p.getUniqueId(), Util.serializeexp(p));
        mysql.setenderchest(p.getUniqueId(), EnderChest.serializeEc(p.getEnderChest().getContents()));
    }
}
