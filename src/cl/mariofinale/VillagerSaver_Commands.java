package cl.mariofinale;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class VillagerSaver_Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            return true;
        }

        Player player = (Player) sender;
        if (player == null) return true;
        if ((args == null || args.length <= 1)) {
            return false;
        }

        switch (args[0].toUpperCase()){
            case "BLACKLISTWORLD":
                return AddWorldToBlackList(player, args[1]);
            case "UNBLACKLISTWORLD":
                return RemoveWorldFromTheBlackList(player, args[1]);
            default:
                return false;
        }
    }

    public static boolean AddWorldToBlackList(Player player, String worldName) {
        ArrayList<String> worldList = new ArrayList<>();
        for (World world : Bukkit.getWorlds()){
            worldList.add(world.getName());
        }
        if (worldList.contains(worldName)){
            if (!villagerSaver.WorldBlackList.contains(worldName)){
                villagerSaver.WorldBlackList.add(worldName);
                SendMessageToPlayer(player, "The World '" + worldName + "' was added to the blacklist.");
            }else{
                SendMessageToPlayer(player, "The World '" + worldName + "' already is on the blacklist.");
            }

        }else {
            SendMessageToPlayer(player, "The World '" + worldName + "' does not exist.");
            String availableWorlds = "";
            for (String wN : worldList){
                availableWorlds = availableWorlds + wN + " - ";
            }
            availableWorlds = availableWorlds.substring(0,availableWorlds.length() - 3).trim();
            SendMessageToPlayer(player, "Available Worlds: " + availableWorlds + "");
        }
        return true;
    }

    public static boolean RemoveWorldFromTheBlackList(Player player, String worldName) {
        if (villagerSaver.WorldBlackList.contains(worldName)){
            villagerSaver.WorldBlackList.remove(worldName);
            SendMessageToPlayer(player, "The World '" + worldName + "' was removed from the blacklist.");
        }else {
            SendMessageToPlayer(player, "The World '" + worldName + "' is not on the blacklist.");
        }
        return true;
    }


    public static boolean SendMessageToPlayer(Player player, String message) {
        if (!player.isOnline()) return false;
        if (player.isBanned()) return false;
        if ((player == null)) return false;

        String resultingMessage = VillagerSaver_PluginVars.PluginPrefix + " " + message;
        player.sendMessage(resultingMessage);
        return true;
    }


}
