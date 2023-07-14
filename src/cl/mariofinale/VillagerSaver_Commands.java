package cl.mariofinale;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Class that handles the commands for the VillagerSaver plugin.
 */
public class VillagerSaver_Commands implements CommandExecutor {


    /**
     * Handles the execution of a command.
     *
     * @param sender  The sender of the command.
     * @param command The command that was executed.
     * @param label   The alias used for the command.
     * @param args    The arguments provided with the command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        // Check if the player is valid
        if (!player.isValid()) return true;
        // Check if there are enough arguments
        if ((args == null || args.length <= 1)) {
            return false;
        }
        // Check the command argument
        switch (args[0].toUpperCase()) {
            case "BLACKLISTWORLD":
                return AddToBlacklistIfWorldExists(player, args[1]);
            case "UNBLACKLISTWORLD":
                return RemoveFromTheBlackListIfWorldExists(player, args[1]);
            default:
                return false;
        }
    }

    /**
     * Adds a world to the blacklist if it exists.
     *
     * @param player    The player executing the command.
     * @param worldName The name of the world to add to the blacklist.
     * @return true if the world was added to the blacklist, false otherwise.
     */
    private static boolean AddToBlacklistIfWorldExists(Player player, String worldName) {
        // Get a list of all available worlds
        HashSet<String> worldList = new HashSet<>();
        for (World world : Bukkit.getWorlds()) {
            worldList.add(world.getName());
        }
        // Check if the specified world exists
        if (worldList.contains(worldName)) {
            // Check if the world is already in the blacklist
            if (!VillagerSaver.WorldBlackList.contains(worldName)) {
                // Add the world to the blacklist
                VillagerSaver.WorldBlackList.add(worldName);
                SendMessageToPlayer(player, "The World '" + worldName + "' was added to the blacklist.");
            } else {
                SendMessageToPlayer(player, "The World '" + worldName + "' is already on the blacklist.");
            }
        } else {
            SendMessageToPlayer(player, "The World '" + worldName + "' does not exist.");
            String availableWorlds = worldList.stream().collect(Collectors.joining(" - "));
            SendMessageToPlayer(player, "Available Worlds: " + availableWorlds);
        }
        return VillagerSaver.WorldBlackList.contains(worldName);
    }

    /**
     * Removes a world from the blacklist if it exists.
     *
     * @param player    The player executing the command.
     * @param worldName The name of the world to remove from the blacklist.
     * @return true if the world was removed from the blacklist, false otherwise.
     */
    private static boolean RemoveFromTheBlackListIfWorldExists(Player player, String worldName) {
        // Check if the world is in the blacklist
        if (VillagerSaver.WorldBlackList.contains(worldName)) {
            // Remove the world from the blacklist
            VillagerSaver.WorldBlackList.remove(worldName);
            SendMessageToPlayer(player, "The World '" + worldName + "' was removed from the blacklist.");
        } else {
            SendMessageToPlayer(player, "The World '" + worldName + "' is not on the blacklist.");
        }
        return true;
    }

    /**
     * Sends a message to a player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    private static void SendMessageToPlayer(Player player, String message) {
        // Check if the player is valid, banned, or online
        if (!player.isValid()) return;
        if (player.isBanned()) return;
        if (!player.isOnline()) return;
        // Construct the resulting message with the plugin prefix
        String resultingMessage = VillagerSaver_PluginVars.PluginPrefix + " " + message;
        // Send the message to the player
        player.sendMessage(resultingMessage);
    }
}
