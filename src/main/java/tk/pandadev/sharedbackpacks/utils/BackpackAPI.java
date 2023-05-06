package tk.pandadev.sharedbackpacks.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tk.pandadev.sharedbackpacks.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BackpackAPI {

    private static final File dataFolder = new File("plugins/SharedBackpacks");
    private static final File backpacksFolder = new File(dataFolder, "backpacks");

    public static final Map<String, ItemStack[]> inventorys = new HashMap<>();


    public static void createConfig(Player player, String backpackName){
        List<UUID> members = new ArrayList<>();
        Inventory inventory = Bukkit.createInventory(null, 54, backpackName);
        inventorys.put(backpackName, inventory.getContents());

        if (!backpacksFolder.exists()) backpacksFolder.mkdirs();
        File backpackFile = new File(backpacksFolder, backpackName + ".yml");
        if (!backpackFile.exists()) {
            try {
                backpackFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(backpackFile);
        config.set("owner", player.getUniqueId().toString());
        config.set("members", members);
        config.set("inventory", inventory.getContents());
        try {
            config.save(backpackFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig(String backpackName){
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static void saveData(Player player, String backpackName, List<String> members, ItemStack[] inventory){
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        config.set("owner", player.getUniqueId().toString());
        config.set("members", members);
        config.set("inventory", inventory);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addMember(Player player, String backpackName, Player member){
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config == null) {
            player.sendMessage(Main.getPrefix() + "§cBackpack not found!");
            return;
        }
        if (member == null){
            player.sendMessage(Main.getInvalidPlayer());
            return;
        }
        UUID ownerUUID = player.getUniqueId();
        UUID newMemberUUID = member.getUniqueId();
        if (!ownerUUID.equals(UUID.fromString(config.getString("owner")))) {
            player.sendMessage(Main.getPrefix() + "§cYou are not the owner of this backpack!");
            return;
        }
        List<String> members = config.getStringList("members");
        if (members.contains(newMemberUUID.toString())) {
            player.sendMessage(Main.getPrefix() + "§cThis player is already a member of this backpack!");
            return;
        }
        members.add(newMemberUUID.toString());
        config.set("members", members);
        saveData(player, backpackName, members, inventorys.get(backpackName));
        player.sendMessage(Main.getPrefix() + "§7The player §a" + member.getName() + " §7was successfully added to §a" + backpackName);
    }

    public static List<String> getBackpackNames() {
        List<String> backpackNames = new ArrayList<>();
        File[] files = backpacksFolder.listFiles();
        if (files == null) {
            return backpackNames;
        }
        for (File file : files) {
            if (file.isFile()) {
                backpackNames.add(file.getName().replace(".yml", ""));
            }
        }
        return backpackNames;
    }

    public static void deleteConfig(Player player, String backpackName) {
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (!getBackpackNames().contains(backpackName)){
            player.sendMessage(Main.getPrefix() + "§cBackpack not found!");
            return;
        }
        if (!playerOwnsBackpack(player, backpackName)){ player.sendMessage(Main.getPrefix() + "§cYou are not the owner of this backpack!"); return;}

        configFile.delete();
        player.sendMessage(Main.getPrefix() + "§a" + backpackName + " §7has been successfully deleted");
    }

    public static void removeMember(Player player, String backpackName, Player member) {
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config == null) {
            player.sendMessage(Main.getPrefix() + "§cBackpack not found!");
            return;
        }
        String ownerUUID = player.getUniqueId().toString();
        if (!ownerUUID.equals(config.getString("owner"))) {
            player.sendMessage(Main.getPrefix() + "§cYou are not the owner of this backpack!");
            return;
        }
        List<String> members = config.getStringList("members");
        String memberUUID = member.getUniqueId().toString();
        if (!members.contains(memberUUID)) {
            player.sendMessage(Main.getPrefix() + "§cThis player is not a member of this backpack!");
            return;
        }
        members.remove(memberUUID);
        config.set("members", members);
        saveData(player, backpackName, members, inventorys.get(backpackName));
        player.sendMessage(Main.getPrefix() + "§7The player §a" + member.getName() + " §7was successfully removed from §a" + backpackName);
    }

    public static boolean playerOwnsBackpack(Player player, String backpackName) {
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config == null) {
            return false;
        }
        return config.getString("owner").equals(player.getUniqueId().toString());
    }

    public static boolean hasBackpacks(Player player) {
        for (String backpackName : getBackpackNames()) {
            File configFile = new File(backpacksFolder, backpackName + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            if (config.getString("owner").equals(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static void renameConfig(Player player, String backpackName, String newName){
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        File newFile = new File(backpacksFolder, newName + ".yml");

        configFile.renameTo(newFile);

        player.sendMessage(Main.getPrefix() + "§a" + backpackName + " §7has been successfully renamed to §a" + newName);
    }

}
