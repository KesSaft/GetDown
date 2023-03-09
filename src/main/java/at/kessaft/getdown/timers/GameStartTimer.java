package at.kessaft.getdown.timers;

import at.kessaft.getdown.Prefixes;
import at.kessaft.getdown.utils.GameManager;
import at.kessaft.getdown.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

public class GameStartTimer extends BukkitRunnable {

    GameManager gameManager;
    private int timer = 60;
    public GameStartTimer(GameManager gameManager, int startTimer) {
        this.gameManager = gameManager;
        timer = startTimer;
    }


    @Override
    public void run() {
        if (timer == 0) {
            this.cancel();
            gameManager.setGameState(GameState.JUMPING);
            return;
        }

        Bukkit.getWorld("world").getPlayers().forEach(p -> {
            if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)
                return;

            ChatColor color = ChatColor.GREEN;

            switch (timer){
                case 3 | 2:
                    color = ChatColor.YELLOW;
                    break;
                case 1:
                    color = ChatColor.RED;
                    break;
            }

            p.sendMessage(Prefixes.getPrefix() + "The Game starts in " + color + ChatColor.BOLD + timer + ChatColor.WHITE + " seconds.");
            p.setLevel(timer);
            p.setExp(timer / 10f);
            if (timer <= 5) {
                p.sendTitle(new Title(color.toString() + ChatColor.BOLD + "" + timer));
                p.playSound(p.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
            }
        });

        timer--;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
