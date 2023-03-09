package at.kessaft.getdown.listeners;

import at.kessaft.getdown.Main;
import at.kessaft.getdown.utils.GameManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private Main main;

    public PlayerListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        for (GameManager gameManager : Main.getInstance().getGameManagers()){
            if (gameManager.joinable()){

                gameManager.playerJoin(event.getPlayer());
                return;
            }else{
                event.getPlayer().kickPlayer(ChatColor.RED + "Game is full");
            }
        }

        refreshPlayerTabList();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Location spawn = (Location) main.getConfiguration().toFileConfiguration().get("lobby.spawn");
        Player player = event.getPlayer();

        player.setBedSpawnLocation(spawn);
        player.teleport(spawn);
        player.setHealth(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);

        for (GameManager gameManager : Main.getInstance().getGameManagers()){
            if (gameManager.hasPlayer(player))
                gameManager.removePlayer(player);
        }

        refreshPlayerTabList();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        //prevent drop
    }

    @EventHandler
    public void onPlayerWorldSwitch(PlayerChangedWorldEvent event){
        if (event.getPlayer().getWorld().equals(Bukkit.getWorld("world"))){
            for (GameManager gameManager : Main.getInstance().getGameManagers()){
                if (gameManager.joinable()){
                    gameManager.playerJoin(event.getPlayer());
                    return;
                }
            }

            event.getPlayer().kickPlayer(ChatColor.RED + "Game is full");
        }

        refreshPlayerTabList();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        event.setFormat((event.getPlayer().isOp() ? ChatColor.RED :  ChatColor.GREEN) + event.getPlayer().getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());

        if (event.getPlayer().isOp() && event.getMessage().startsWith("@a")){
            event.setMessage(event.getMessage().replace("@a", "").trim());
            return;
        }

        Player player = event.getPlayer();
        World world = player.getWorld();

        for (Player recipient : Main.getInstance().getServer().getOnlinePlayers()){
            if (recipient.getWorld().equals(world))
                recipient.sendMessage(event.getMessage());
        }

        event.setCancelled(true);
    }

    public void refreshPlayerTabList(){
        for (Player player : Bukkit.getOnlinePlayers()){
            for (Player pl : Bukkit.getOnlinePlayers()){
                if (pl.getWorld().equals(player.getWorld()))
                    pl.showPlayer(player);
                else
                    pl.hidePlayer(player);
            }
        }
    }
}
