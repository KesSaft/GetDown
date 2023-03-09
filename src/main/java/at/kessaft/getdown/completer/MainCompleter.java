package at.kessaft.getdown.completer;

import at.kessaft.getdown.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCompleter implements TabCompleter {
    private ArrayList<String> lore = new ArrayList<>();
    private Main main;

    public MainCompleter(Main main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        FileConfiguration mc = main.getMapsConfig().toFileConfiguration();

        if (!(sender instanceof Player) || !sender.isOp())
            return null;

        if (args.length == 1){
            lore.clear();

            lore.add("help");
            lore.add("lobby");
            lore.add("shopLobby");
            lore.add("map");

            return lore;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("lobby")){
            lore.clear();

            lore.add("setSpawn");

            return lore;
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("map") || args[0].equalsIgnoreCase("shopLobby"))){
            lore.clear();

            lore.add("create");
            lore.add("delete");
            lore.add("edit");

            return lore;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("shopLobby") && (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("edit"))) {
            lore.clear();

            lore = new ArrayList<String>(mc.getConfigurationSection("shopLobbies").getKeys(false));

            return lore;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("map") && (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("edit"))) {
            lore.clear();

            lore = new ArrayList<String>(mc.getConfigurationSection("jumpMaps").getKeys(false));

            return lore;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("shopLobby") && args[1].equalsIgnoreCase("edit")) {
            lore.clear();

            lore.add("setSpawn");

            return lore;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("map") && args[1].equalsIgnoreCase("edit")) {
            lore.clear();

            lore.add("setSpawn");
            lore.add("setBottomCenter");
            lore.add("setHeight");
            lore.add("setRadius");
            lore.add("setBottomSpace");
            lore.add("setBlocks");

            return lore;
        }

        return null;
    }
}
