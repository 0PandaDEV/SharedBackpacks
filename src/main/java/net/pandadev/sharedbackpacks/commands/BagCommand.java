package net.pandadev.sharedbackpacks.commands;

import net.pandadev.sharedbackpacks.Main;
import net.pandadev.sharedbackpacks.utils.BackpackAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BagCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§6This command can only be run by a player!");
            return false;
        }
        Player player = (Player) (sender);

        if (args.length == 1){
            if (!BackpackAPI.getBackpackNames().contains(args[0])) {
                player.sendMessage(Main.getPrefix() + "§cThis backpack does not exists");
                return false;
            }

            BackpackAPI.openBackpackGui(player, args[0]);
        } else {
            player.sendMessage(Main.getPrefix() + "§c/bag <backpack>");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        Player playert = (Player) (sender);

        if (args.length == 1) {
            list.addAll(BackpackAPI.getAccessibleBackpackNames(playert));
        }

        ArrayList<String> completerList = new ArrayList<String>();
        String currentarg = args[args.length - 1].toLowerCase();
        for (String s : list) {
            String s1 = s.toLowerCase();
            if (!s1.startsWith(currentarg)) continue;
            completerList.add(s);
        }

        return completerList;
    }

}