package weathercode.chaneels.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import weathercode.chaneels.events.GuiClick;
import weathercode.chaneels.events.JoinQuitEvent;
import weathercode.chaneels.mysql.mysql;
import weathercode.chaneels.commands.ch;

public final class Main extends JavaPlugin {

    private static Main instance;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        new mysql(this);
        mysql.setConn();
        mysql.createTable();
        mysql.createTable2();
        new ch(this);
        if(!mysql.ischaneeladded(getConfig().getString("ActualChaneel.ChaneelID"))) {
            mysql.addchaneel(getConfig().getString("ActualChaneel.ChaneelID"), "20", Bukkit.getOnlinePlayers().size(), "ONLINE");
        }
        mysql.setstatus(getConfig().getString("ActualChaneel.ChaneelID"), "ONLINE");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new JoinQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new GuiClick(), this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
           mysql.setstatus(getConfig().getString("ActualChaneel.ChaneelID"), "ONLINE");
           mysql.settps(getConfig().getString("ActualChaneel.ChaneelID"), 20);
           mysql.setplayers(getConfig().getString("ActualChaneel.ChaneelID"), Bukkit.getOnlinePlayers().size());
        }, 0, 120);
        instance = this;

    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        mysql.setstatus(getConfig().getString("ActualChaneel.ChaneelID"), "OFFLINE");
    }
}
