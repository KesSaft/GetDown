package at.kessaft.getdown.commands;

import at.kessaft.getdown.Main;
import at.kessaft.getdown.Prefixes;
import at.kessaft.getdown.utils.GameManager;
import at.kessaft.getdown.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    private Main main;

    public StartCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()){
            sender.sendMessage(Prefixes.getError() + "Sorry, you need to be an Administrator to use this command.");
            return true;
        }

        if (sender instanceof Player && !((Player) sender).getWorld().equals(Bukkit.getWorld("world"))){
            sender.sendMessage(Prefixes.getError() + "Sorry, you need to be in the GetDown Lobby to start a game.");
            return true;
        }

        for (GameManager gameManager : main.getGameManagers()){
            if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.STARTING)){
                gameManager.setGameState(GameState.START);
                sender.sendMessage(Prefixes.getSuccess() + "The game is starting in 10 seconds.");
            }else {
                sender.sendMessage(Prefixes.getError() + "The game is starting in 10 seconds.");
            }
        }

        return false;
    }
}
