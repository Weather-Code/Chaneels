package weathercode.chaneels.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import weathercode.chaneels.main.Main;

public class SendServer {
    public static void sendtoserver(Player p, String server) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("Connect");
        dataOutput.writeUTF(server);
        p.sendPluginMessage(Main.getInstance(), "BungeeCord", dataOutput.toByteArray());
    }
}
