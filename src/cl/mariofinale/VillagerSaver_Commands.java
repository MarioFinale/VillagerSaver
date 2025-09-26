package cl.mariofinale;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles command execution for the VillagerSaver plugin, allowing players to manage the world blacklist.
 * Supports adding and removing worlds from the blacklist via the "/villagersaver" command.
 * Requires the "villagersaver.blacklist" permission to execute blacklist-modifying commands.
 */
public class VillagerSaver_Commands implements CommandExecutor {
    private final VillagerSaver plugin;

    /**
     * Constructs a new command handler with a reference to the main plugin instance.
     *
     * @param plugin The VillagerSaver plugin instance, used to access and modify the world blacklist
     */
    public VillagerSaver_Commands(VillagerSaver plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the VillagerSaver command based on the provided arguments.
     * Supports "blacklistworld" to add a world to the blacklist and "unblacklistworld" to remove one.
     * Only players with the "villagersaver.blacklist" permission can modify the blacklist.
     * Non-player senders (e.g., console) and invalid players are rejected with appropriate feedback.
     *
     * @param sender  The entity that issued the command (must be a player)
     * @param command The command object representing "villagersaver"
     * @param label   The alias used to execute the command (e.g., "villagersaver")
     * @param args    The arguments provided with the command (minimum 2 expected for valid execution)
     * @return true if the command was handled successfully (including error cases), false otherwise
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }
        if (!player.isValid()) return true;

        if (!player.hasPermission("villagersaver.blacklist")) {
            sendMessageToPlayer(player, "You don’t have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sendMessageToPlayer(player, "Usage: /villagersaver <blacklistworld|unblacklistworld> <world>");
            return true;
        }

        return switch (args[0].toUpperCase()) {
            case "BLACKLISTWORLD" -> tryAddWorldToBlacklist(player, args[1]);
            case "UNBLACKLISTWORLD" -> removeWorldFromBlacklist(player, args[1]);
            default -> {
                sendMessageToPlayer(player, "Unknown subcommand. Use blacklistworld or unblacklistworld.");
                yield true;
            }
        };
    }

    /**
     * Attempts to add a world to the blacklist if it exists on the server.
     * Sends feedback to the player indicating whether the world was added, already blacklisted, or doesn’t exist.
     *
     * @param player    The player executing the command
     * @param worldName The name of the world to add to the blacklist
     * @return true if the operation was successful (world exists and was processed), false if the world doesn’t exist
     */
    private boolean tryAddWorldToBlacklist(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) { // Check if world exists
            sendMessageToPlayer(player, "The world '" + worldName + "' does not exist.");
            return false;
        }
        if (!plugin.getWorldBlackList().contains(worldName)) {
            plugin.getWorldBlackList().add(worldName);
            sendMessageToPlayer(player, "The world '" + worldName + "' was added to the blacklist.");
        } else {
            sendMessageToPlayer(player, "The world '" + worldName + "' is already blacklisted.");
        }
        return true;
    }

    /**
     * Removes a world from the blacklist if it is currently listed.
     * Sends feedback to the player indicating whether the world was removed or wasn’t blacklisted.
     * Does not check if the world exists on the server, only if it’s in the blacklist.
     *
     * @param player    The player executing the command
     * @param worldName The name of the world to remove from the blacklist
     * @return true if the operation was successful (regardless of whether the world was removed)
     */
    private boolean removeWorldFromBlacklist(Player player, String worldName) {
        if (plugin.getWorldBlackList().contains(worldName)) {
            plugin.getWorldBlackList().remove(worldName);
            sendMessageToPlayer(player, "The world '" + worldName + "' was removed from the blacklist.");
        } else {
            sendMessageToPlayer(player, "The world '" + worldName + "' is not on the blacklist.");
        }
        return true;
    }

    /**
     * Sends a formatted message to the player with the plugin prefix, if the player is valid and online.
     * No message is sent if the player is invalid, banned, or offline.
     *
     * @param player  The player to receive the message
     * @param message The message content to send (appended to the plugin prefix)
     */
    private static void sendMessageToPlayer(Player player, String message) {
        if (!player.isValid() || player.isBanned() || !player.isOnline()) return;
        String resultingMessage = VillagerSaver_PluginVars.PluginPrefix + " " + message;
        player.sendMessage(resultingMessage);
    }
}