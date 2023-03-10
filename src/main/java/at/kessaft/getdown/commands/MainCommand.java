package at.kessaft.getdown.commands;

import at.kessaft.getdown.Main;
import at.kessaft.getdown.Prefixes;
import at.kessaft.getdown.utils.JumpMap;
import at.kessaft.getdown.utils.Position;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class MainCommand implements CommandExecutor {
    private Main main;

    public MainCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Prefixes.getError() + "Sorry, you need to be an Administrator to use this command.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Prefixes.getError() + "Sorry, you need to be a Player to use this command.");
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration mc = main.getMapsConfig().toFileConfiguration();
        FileConfiguration cc = main.getConfiguration().toFileConfiguration();

        if (args.length >= 2 && args[0].equalsIgnoreCase("lobby")) {
            if (args[1].equalsIgnoreCase("setspawn")) {
                cc.set("lobby.spawn", player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
                player.sendMessage(Prefixes.getSuccess() + "You have successfully set the lobby spawn to your Location.");

                main.getConfiguration().save();
                return true;
            }
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("shopLobby")) {
            if (args[1].equalsIgnoreCase("create")) {
                LinkedHashMap<String, Location> shopLobbies = (LinkedHashMap<String, Location>) mc.getConfigurationSection("shopLobbies");

                if (!shopLobbies.isEmpty() && shopLobbies.keySet().contains(args[2].trim())) {
                    sender.sendMessage(Prefixes.getError() + "A ShopLobby with this name already exists.");
                    return true;
                }

                shopLobbies.put(args[2].trim(), player.getLocation());
                mc.createSection("shopLobbies", shopLobbies);
                player.sendMessage(Prefixes.getSuccess() + "New ShopLobby created.");

                main.getMapsConfig().save();
            } else if (args[1].equalsIgnoreCase("delete")) {
                LinkedHashMap<String, Location> shopLobbies = (LinkedHashMap<String, Location>) mc.getConfigurationSection("shopLobbies");

                if (!shopLobbies.isEmpty() && !shopLobbies.keySet().contains(args[2].trim())) {
                    sender.sendMessage(Prefixes.getError() + "There does no ShopLobby exist with this name.");
                    return true;
                }

                shopLobbies.remove(args[2].trim());
                mc.createSection("shopLobbies", shopLobbies);
                player.sendMessage(Prefixes.getSuccess() + "ShopLobby delete.");

                main.getMapsConfig().save();
            } else if (args.length >= 4 && args[1].equalsIgnoreCase("edit")) {
                LinkedHashMap<String, Location> shopLobbies = (LinkedHashMap<String, Location>) mc.getConfigurationSection("shopLobbies");

                if (!shopLobbies.isEmpty() && !shopLobbies.keySet().contains(args[2].trim())) {
                    sender.sendMessage(Prefixes.getError() + "There does no ShopLobby exist with this name.");
                    return true;
                }

                Location shopLobby = shopLobbies.get(args[2].trim());

                switch (args[3].toLowerCase()) {
                    case "setspawn":
                        shopLobby = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                        shopLobby.setWorld(null);
                        player.sendMessage(Prefixes.getSuccess() + "Set Spawn to current player position.");
                        break;
                }

                shopLobbies.put(args[2].trim(), shopLobby);
                mc.createSection("shopLobbies", shopLobbies);

                main.getMapsConfig().save();
            }
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("jumpMap")) {
            LinkedHashMap<String, JumpMap> maps = new LinkedHashMap<>();
            if (mc.getConfigurationSection("jumpMaps") != null)
                maps.putAll((LinkedHashMap<String, JumpMap>) mc.getMapList("jumpMaps"));

            if (args[1].equalsIgnoreCase("create")) {
                if (maps != null && !maps.isEmpty() && maps.keySet().contains(args[2].trim())) {
                    sender.sendMessage(Prefixes.getError() + "A jumpMap with this name already exists.");
                    return true;
                }

                maps.put(args[2].trim(), new JumpMap());
                mc.createSection("jumpMaps", maps);
                player.sendMessage(Prefixes.getSuccess() + "New jumpMap created.");

                main.getMapsConfig().save();
            } else if (args[1].equalsIgnoreCase("delete")) {
                if (!maps.isEmpty() && !maps.keySet().contains(args[2].trim())) {
                    sender.sendMessage(Prefixes.getError() + "There does no jumpMap exist with this name.");
                    return true;
                }

                maps.remove(args[2].trim());
                mc.createSection("jumpMaps", maps);
                player.sendMessage(Prefixes.getSuccess() + "JumpMap got deleted.");

                main.getMapsConfig().save();
            } else if (args.length >= 4 && args[1].equalsIgnoreCase("edit")) {
                if (!maps.isEmpty() && !maps.isEmpty() && !maps.keySet().contains(args[2].trim())) {
                    sender.sendMessage(Prefixes.getError() + "There does no jumpMap exist with this name.");
                    return true;
                }

                JumpMap map = maps.get(args[2].trim());

                switch (args[3].toLowerCase()) {
                    case "setspawn":
                        map.setSpawn(new Position(player.getLocation(), false));
                        player.sendMessage(Prefixes.getSuccess() + "Set Spawn to current player position.");
                        break;
                    case "setbottomcenter":
                        map.setBottomCenter(new Position(player.getLocation(), false));
                        player.sendMessage(Prefixes.getSuccess() + "Set Bottom to current player height.");
                        break;
                    case "setheight":
                        map.setHeight(player.getLocation().getBlock().getY());
                        player.sendMessage(Prefixes.getSuccess() + "Set Height to current player height.");
                        break;
                    case "setbottomspace":
                        try {
                            if (args.length < 5){
                                player.sendMessage(Prefixes.getError() + "This Command is not complete.");
                                return true;
                            }

                            map.setBottomSpace(Integer.parseInt(args[4].trim()));
                            player.sendMessage(Prefixes.getSuccess() + "Set Bottom Space");
                        } catch (Exception e) {
                            e.printStackTrace();
                            player.sendMessage(Prefixes.getError() + "Something went wrong.");
                            return true;
                        }
                        break;
                    case "setradius":
                        try {
                            if (args.length < 5)
                                return true;

                            map.setRadius(Integer.parseInt(args[4].trim()));
                            player.sendMessage(Prefixes.getSuccess() + "Set Radius");
                        } catch (Exception e) {
                            e.printStackTrace();
                            player.sendMessage(Prefixes.getError() + "Something went wrong.");
                            return true;
                        }
                        break;
                    case "setBlocks":
                        try {
                            if (args.length < 5)
                                return true;

                            if (!player.getInventory().getItemInHand().getType().isBlock() ||
                                    player.getInventory().getItemInHand().getType().hasGravity()){
                                player.sendMessage(Prefixes.getError() + "This is not valid Block for GetDown.");
                                return true;
                            }

                            map.addBlock(player.getInventory().getItemInHand().getType(), Integer.parseInt(args[4].trim()));
                            player.sendMessage(Prefixes.getSuccess() + "Added/Removed Block");
                        } catch (Exception e) {
                            e.printStackTrace();
                            player.sendMessage(Prefixes.getError() + "Something went wrong.");
                            return true;
                        }
                        break;
                    default:
                        player.sendMessage(Prefixes.getError() + "This Command is not complete.");
                        break;
                }

                maps.put(args[2].trim(), map);
                    mc.createSection("jumpMaps", maps);

                main.getMapsConfig().save();
            }
        } else {
            player.sendMessage(Prefixes.getError() + "This Command is not complete.");
        }

        return false;
    }
}
