package net.pandadev.sharedbackpacks.commands;

import net.pandadev.sharedbackpacks.Main;
import net.pandadev.sharedbackpacks.guis.Guis;
import net.pandadev.sharedbackpacks.utils.BackpackAPI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackpacksCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§6This command can only be run by a player!");
            return false;
        }
        Player player = (Player) (sender);


        if (args.length == 1 && args[0].equalsIgnoreCase("create")) {

            if (!player.hasPermission("sharedbackpacks.config.create")){player.sendMessage(Main.getNoPerm()); return false;}

            new AnvilGUI.Builder()
                    .onClick((state, text) -> {

                        BackpackAPI.createBackpack(player, text.getText());

                        player.sendMessage(Main.getPrefix() + "§7Backpack named §a" + text.getText() + " §7was successfully created");

                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    })
                    .preventClose()
                    .itemLeft(new ItemStack(Material.NAME_TAG))
                    .title("Enter the name")
                    .plugin(Main.getInstance())
                    .open(player);

        } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {

            if (!player.hasPermission("sharedbackpacks.config.add")){player.sendMessage(Main.getNoPerm()); return false;}

            if (!BackpackAPI.hasBackpack(player)) {
                player.sendMessage(Main.getNoBackpack());
                return false;
            }
            BackpackAPI.addMember(player, args[2], Bukkit.getPlayer(args[1]));

        } else if (args.length == 3 && args[0].equalsIgnoreCase("remove")){

            if (!player.hasPermission("sharedbackpacks.config.remove")){player.sendMessage(Main.getNoPerm()); return false;}

            if (!BackpackAPI.hasBackpack(player)) {
                player.sendMessage(Main.getNoBackpack());
                return false;
            }
            BackpackAPI.removeMember(player, args[2], Bukkit.getPlayer(args[1]));

        } else if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {

            if (!player.hasPermission("sharedbackpacks.config.gui")){player.sendMessage(Main.getNoPerm()); return false;}

            Guis.backpackConfig(player);

        }
//        else if (args.length == 2 && args[0].equalsIgnoreCase("delete")){
//
//            if (!player.hasPermission("sharedbackpacks.config.delete")){player.sendMessage(Main.getNoPerm()); return false;}
//
//            BackpackAPI.deleteConfig(player, args[1]);
//
//        } else if (args.length == 2 && args[0].equalsIgnoreCase("rename")){
//
//            if (!player.hasPermission("sharedbackpacks.config.rename")){player.sendMessage(Main.getNoPerm()); return false;}
//
//            new AnvilGUI.Builder()
//                    .onClick((state, text) -> {
//
//                        BackpackAPI.renameConfig(player, args[1], text.getText());
//
//                        Main.refreshMap();
//
//                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
//                    })
//                    .preventClose()
//                    .itemLeft(new ItemStack(Material.NAME_TAG))
//                    .title("Enter the name")
//                    .text(args[1])
//                    .plugin(Main.getInstance())
//                    .open(player);
//
//        }
        else {
            player.sendMessage(Main.getPrefix() + "§6/backpack create <name>\n                   §6/backpack add <player> <backpack>\n                   §6/backpack remove <player> <backpack>\n                   §6/backpack rename <backpack>\n                   §6/backpack delete <backpack>\n                   §6/backpack gui");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        Player playert = (Player) (sender);

        if (args.length == 1){
            list.add("create");
            list.add("add");
            list.add("remove");
            list.add("gui");
//            list.add("delete");
//            list.add("rename");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("add") || args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                list.add(players.getName());
            }
            list.remove(playert.getName());
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("add") || args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            list.addAll(BackpackAPI.getOwnedBackpackNames(playert));
        }
//
//        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
//            list.addAll(BackpackAPI.getBackpackNames());
//        }
//
//        if (args.length == 2 && args[0].equalsIgnoreCase("rename")) {
//            list.addAll(BackpackAPI.getBackpackNames());
//        }

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