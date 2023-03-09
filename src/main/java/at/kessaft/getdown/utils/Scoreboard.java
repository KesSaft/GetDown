package at.kessaft.getdown.utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Scoreboard {

    public static void setTabListInfo(Player player){
        String header = ChatColor.GREEN + "KesSaft.tk" + ChatColor.GRAY + " - " + ChatColor.AQUA + "GetDown";
        String footer = ChatColor.GRAY + "To report a player, please use the " + ChatColor.RED.toString() + ChatColor.BOLD + "/report" + ChatColor.GRAY + " command";

        player.setPlayerListHeaderFooter(new TextComponent(header), new TextComponent(footer));
    }
}
