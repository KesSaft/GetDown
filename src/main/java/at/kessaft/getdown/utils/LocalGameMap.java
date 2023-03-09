package at.kessaft.getdown.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class LocalGameMap implements GameMap {
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public LocalGameMap(File worldFolder, String worldName, boolean loadOnInit){
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

        if (loadOnInit) load();
    }

    //Untility

    public  boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis()
        );

        try {
            FileUtil.copy(sourceWorldFolder, activeWorldFolder);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Fails to load GameMap from source folder " + sourceWorldFolder);
            e.printStackTrace();
            return false;
        }

        this.bukkitWorld = Bukkit.createWorld(
                new WorldCreator(activeWorldFolder.getName())
        );

        if (bukkitWorld != null) this.bukkitWorld.setAutoSave(false);
        return isLoaded();
    }

    public void unload() {
        if (bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        if (activeWorldFolder != null) FileUtil.delete(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    public boolean restoreFromSource() {
        unload();
        return load();
    }

    //Accessors

    public boolean isLoaded() {
        return getWorld() != null;
    }

    public World getWorld() {
        return bukkitWorld;
    }
}
