package at.kessaft.getdown.commands;

import at.kessaft.getdown.Main;
import at.kessaft.getdown.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommnd implements CommandExecutor {
    private Main main;

    public WarpCommnd(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()){
            sender.sendMessage(Prefixes.getError() + "Sorry, you can't use this command!");
            return true;
        }

        ((Player) sender).getPlayer().setGameMode(GameMode.CREATIVE);

        if (!((Player)sender).getWorld().equals(Bukkit.getWorld("world"))){
            ((Player) sender).getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0, 120, 0));
            return true;
        }

        ((Player) sender).getPlayer().teleport(new Location(main.getGameManagers().get(0).getMap().getWorld(), 0, 200, 0));
        return true;
    }
}
