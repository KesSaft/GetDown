package at.kessaft.getdown;

import at.kessaft.getdown.commands.MainCommand;
import at.kessaft.getdown.commands.StartCommand;
import at.kessaft.getdown.commands.WarpCommnd;
import at.kessaft.getdown.completer.MainCompleter;
import at.kessaft.getdown.listeners.BlockListener;
import at.kessaft.getdown.listeners.DamageListener;
import at.kessaft.getdown.listeners.PlayerListener;
import at.kessaft.getdown.utils.Config;
import at.kessaft.getdown.utils.GameManager;
import at.kessaft.getdown.utils.JumpMap;
import at.kessaft.getdown.utils.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Config config, maps;
    private ArrayList<GameManager> gameManagers;
    File gameMapsFolder;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(JumpMap.class);
        ConfigurationSerialization.registerClass(Position.class);

        saveDefaultConfig();
        config = new Config("config.yml", getDataFolder());
        maps = new Config("maps.yml", getDataFolder());

        checkForOptions();
        listenerRegistration();
        commandRegistration();

        loadMap();
    }

    @Override
    public void onDisable() {
        config.save();
        for (GameManager gm : gameManagers){
            gm.getMap().unload();
        }
    }



    private void loadMap(){
        getDataFolder().mkdirs();

        gameMapsFolder = new File(getDataFolder(), "gameMaps");
        if (!gameMapsFolder.exists()){
            gameMapsFolder.mkdirs();
        }

        this.gameManagers = new ArrayList<>();
        this.gameManagers.add(new GameManager(this));
    }

    private void listenerRegistration(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BlockListener(instance), this);
        pluginManager.registerEvents(new DamageListener(instance), this);
        pluginManager.registerEvents(new PlayerListener(instance), this);
    }

    private void commandRegistration(){
        getCommand("warp").setExecutor(new WarpCommnd(instance));
        getCommand("start").setExecutor(new StartCommand(instance));
        getCommand("getdown").setExecutor(new MainCommand(instance));

        getCommand("getdown").setTabCompleter(new MainCompleter(instance));
    }

    private void checkForOptions(){
        config.IfNotExist("lobby.spawn", new Location(Bukkit.getWorld("world"), 0.5, 100, 0.5));
        config.IfNotExist("options.maxPlayers", 12);
        maps.IfNotExist("jumpMaps", new LinkedHashMap<String, JumpMap>());
        maps.IfNotExist("shopLobbies", new LinkedHashMap<String, Location>());
    }

    public static Main getInstance() {
        return instance;
    }

    public ArrayList<GameManager> getGameManagers() {
        return this.gameManagers;
    }

    public File getGameMapsFolder() {
        return gameMapsFolder;
    }

    public Config getConfiguration() {
        return config;
    }

    public Config getMapsConfig() {
        return maps;
    }
}
