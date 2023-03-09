package at.kessaft.getdown.utils;

import at.kessaft.getdown.Main;
import at.kessaft.getdown.Prefixes;
import at.kessaft.getdown.timers.GameStartTimer;
import at.kessaft.getdown.timers.LobbyCountdownTimer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {
    private final Main main;
    private GameState gameState = GameState.LOBBY;
    private LobbyCountdownTimer lobbyCountdownTimer;
    private GameStartTimer gameStartTimer;
    private Map<UUID, PlayerData> players = new LinkedHashMap<>();
    private GameMap map;

    private ArrayList<String> maps = new ArrayList<>();
    private String shopLobby;
    private int currentMap = -1;

    public GameManager(Main main) {
        this.main = main;
        map = new LocalGameMap(main.getGameMapsFolder(), "Maps", true);
        FileConfiguration mc = main.getMapsConfig().toFileConfiguration();

        ArrayList<String> allMaps = new ArrayList<>();

        if (mc.contains("jumpMaps"))
            return;

        allMaps.addAll(mc.getConfigurationSection("jumpMaps").getKeys(false));

        for (int i=0; i > 3; i++) {
            if (allMaps.isEmpty() || allMaps.size() == 0)
                break;

            int rnd = new Random().nextInt(allMaps.size());
            maps.add(allMaps.get(rnd));
            allMaps.remove(rnd);
        }

        LinkedHashMap<String, Location> shopLobbies = (LinkedHashMap<String, Location>) main.getMapsConfig().toFileConfiguration().getConfigurationSection("shopLobbies");
        if (shopLobbies != null && !shopLobbies.isEmpty()) {
            int lobby = new Random().nextInt(shopLobbies.size());
            shopLobby = new ArrayList<>(shopLobbies.keySet()).get(lobby);
        }
    }

    public void setGameState(GameState gameState) {
        if (shopLobby == null || maps.size() == 0 || map == null || players.size() == 0)
            return;

        if (this.gameState == gameState || (this.gameState.ordinal() > gameState.ordinal() && !this.gameState.equals(GameState.JUMPEND))) return;

        this.gameState = gameState;

        switch (gameState) {
            case LOBBY:
                break;
            case STARTING:
                lobbyCountdownTimer = new LobbyCountdownTimer(this, 60);
                lobbyCountdownTimer.runTaskTimer(main, 0, 20);

                break;
            case START:
                Location lobby = ((LinkedHashMap<String, Location>) main.getMapsConfig().toFileConfiguration().getConfigurationSection("jumpMaps")).get(shopLobby);
                currentMap = 0;

                for (Player player : Bukkit.getWorld("world").getPlayers()) {
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
                        continue;

                    players.put(player.getUniqueId(), new PlayerData());
                    player.teleport(new Location(map.getWorld(), lobby.getX(), lobby.getY(), lobby.getZ()));
                }

                if (lobbyCountdownTimer != null)
                    lobbyCountdownTimer.cancel();

                gameStartTimer = new GameStartTimer(this, 10);
                gameStartTimer.runTaskTimer(main, 0, 20);

                setupMap(maps.get(currentMap));
                break;
            case JUMPING:
                if (hasMapLeft())
                    currentMap ++;
                else {
                    setGameState(GameState.FIGHT);
                    return;
                }

                for (Player player : map.getWorld().getPlayers()) {
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
                        continue;

                    player.setGameMode(GameMode.ADVENTURE);
                    player.setHealth(20);
                    player.getInventory().clear();
                }
                break;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void playerJoin(Player player) {
        if (!joinable()) return;

        if (!players.containsKey(player.getUniqueId()))
            players.put(player.getUniqueId(), new PlayerData());

        Location spawn = (Location) main.getConfiguration().toFileConfiguration().get("lobby.spawn");

        player.setBedSpawnLocation(spawn);
        player.teleport(spawn);
        player.setHealth(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor("GetDown");
        bookMeta.setTitle(ChatColor.AQUA + "How to Play");
        bookMeta.setDisplayName(ChatColor.AQUA + "How to Play");
        bookMeta.addPage(ChatColor.AQUA + "---- GETDOWN ----\n\n" + ChatColor.BLACK + "GetDown is a Minigame that involves players racing to the bottom of a tower as quickly as possible, while also collecting coins and other players.");
        bookMeta.addPage(ChatColor.AQUA + "How to Play\n\n" + ChatColor.BLACK + "You start at the to of the Tower. By jumping from block to block you will get down and on the way you can earn coins by jumping on a sealantern or get a effect when standing on a goldblock. The First who is at the bottom wins and gets some extra coins.");
        bookMeta.addPage("After tree Towers the jumping phase is over and you will have one minute to buy equipment with your coins from the store.\n\n" + ChatColor.GREEN + "Than the fight begins and the last one standing wins.");

        book.setItemMeta(bookMeta);

        player.getInventory().setItem(8, book);

        Scoreboard.setTabListInfo(player);
    }

    public void removePlayer(Player player) {
        if (players.containsKey(player.getUniqueId()))
        players.remove(player.getUniqueId());
    }

    public boolean hasPlayer(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public boolean joinable() {
        return ((gameState.equals(GameState.LOBBY) || gameState.equals(GameState.STARTING)) && players.size() <= (int) main.getConfiguration().toFileConfiguration().get("options.maxPlayers"));
    }

    public boolean hasMapLeft() {
        return currentMap < maps.size() -1;
    }

    public GameMap getMap() {
        return map;
    }

    private void setupMap(String name){
        LinkedHashMap<String, JumpMap> allMaps = (LinkedHashMap<String, JumpMap>) main.getMapsConfig().toFileConfiguration().getConfigurationSection("jumpMaps");

        if (allMaps.isEmpty() || !allMaps.containsKey(name))
            return;

        JumpMap loadingMap = allMaps.get(name);
        int maxPercentage = loadingMap.getBlocks().values().stream()
                .mapToInt(e -> e)
                .sum();

        new BukkitRunnable() {
            int currentHeight = (int)loadingMap.getBottomCenter().getX() + loadingMap.getBottomSpace();
            @Override
            public void run() {
                for (int h = currentHeight; h < loadingMap.getHeight(); h++){
                    for (int x = (int)loadingMap.getBottomCenter().getX() - loadingMap.getRadius(); x < loadingMap.getBottomCenter().getX() + loadingMap.getRadius(); x++){
                        for (int z = (int)loadingMap.getBottomCenter().getZ() - loadingMap.getRadius(); z < loadingMap.getBottomCenter().getZ() + loadingMap.getRadius(); z++){
                            if(Math.pow((double)x - (double)loadingMap.getBottomCenter().getX(), 2) + Math.pow((double)z - (double)loadingMap.getBottomCenter().getZ(), 2) > Math.pow(loadingMap.getRadius(), 2)) continue;

                            if (map.getWorld().getBlockAt(x, h, z).getType() != Material.AIR) continue;

                            int random = new Random().nextInt(maxPercentage * 10);

                            if (random > maxPercentage)
                                continue;

                            int current = 0;
                            for (Material mat : loadingMap.getBlocks().keySet()){
                                if (current + random > loadingMap.getBlocks().get(mat)){
                                    map.getWorld().getBlockAt(x, h, z).setType(mat);
                                    break;
                                }
                                current += random;
                            }
                        }
                    }

                    if (h > currentHeight + 1){
                        currentHeight = h;
                        break;
                    }
                }
            }
        }.runTaskTimer(main, 0, 1);

        System.out.println(Prefixes.getSuccess() + "Map " + name + " has finished loading.");
    }
}
