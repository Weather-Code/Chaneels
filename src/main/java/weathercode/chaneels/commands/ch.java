package weathercode.chaneels.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import weathercode.chaneels.Utils.Util;
import weathercode.chaneels.guis.chgui;
import weathercode.chaneels.main.Main;


public class ch implements CommandExecutor {
    private static Main plugins;

    public ch(Main plugins) {
        ch.plugins = plugins;
        plugins.getCommand("ch").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        if(sender instanceof Player) {
            chgui.opengui(p);
        } else {
            sender.sendMessage(Util.helpcolor("&4BLAD: &cNie mozesz uzyc tej komendy w konsoli serwera"));
        }
        return false;
    }
}
