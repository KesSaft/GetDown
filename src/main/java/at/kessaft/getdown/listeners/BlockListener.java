package at.kessaft.getdown.listeners;

import at.kessaft.getdown.Main;
import at.kessaft.getdown.utils.GameManager;
import at.kessaft.getdown.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class BlockListener implements Listener {
    private Main main;

    public BlockListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (event.getPlayer().getWorld() == Bukkit.getWorld("world") && (!event.getPlayer().isOp() || !event.getPlayer().getGameMode().equals(GameMode.CREATIVE))) {
            event.setCancelled(true);
            return;
        }

        for (GameManager gm : Main.getInstance().getGameManagers()){
            ArrayList<Material> allowedToBreak = new ArrayList<>(); // Config.get("");

            if (gm.hasPlayer(event.getPlayer())){
                if (!gm.getGameState().equals(GameState.FIGHT) || !allowedToBreak.contains(event.getBlock().getType())){
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (event.getPlayer().getWorld() == Bukkit.getWorld("world") && (!event.getPlayer().isOp() || !event.getPlayer().getGameMode().equals(GameMode.CREATIVE))) {
            event.setCancelled(true);
            return;
        }
    }
}
