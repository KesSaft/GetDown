package at.kessaft.getdown;

import org.bukkit.ChatColor;

public class Prefixes {

    public static String getPrefix(){
        return ChatColor.GRAY + "[" + ChatColor.AQUA + "GetDown" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    }

    public static String getSuccess(){
        return getPrefix() + ChatColor.GREEN;
    }

    public static String getError(){
        return getPrefix() + ChatColor.RED;
    }
}
