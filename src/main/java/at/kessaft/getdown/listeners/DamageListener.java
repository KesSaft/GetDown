package at.kessaft.getdown.listeners;

import at.kessaft.getdown.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
    private Main main;

    public DamageListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity().getWorld() == Bukkit.getWorld("world")){
            event.setCancelled(true);
            return;
        }
    }
}
