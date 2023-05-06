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

    public static void createConfig(Player owner, String backpackName) {
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
        config.set("owner", owner.getUniqueId().toString());
        config.set("inventory", new HashMap<>());
        try {
            config.save(backpackFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static FileConfiguration getConfig(String backpackName) {
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        if (!configFile.exists()) {
            return null;
        }
        return YamlConfiguration.loadConfiguration(configFile);
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

    public static void saveConfig(String backpackName, FileConfiguration config) {
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        try {
            config.save(configFile);
            System.out.println("Inventory saved to " + backpackName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveInventory(FileConfiguration config, Inventory inventory, String backpackName) {
        config.set("inventory", inventory.getContents());
        saveConfig(backpackName, config);
    }

    public static Inventory loadInventory(FileConfiguration config, String backpackName) {
        Inventory inventory = Bukkit.createInventory(null, 54, backpackName);
        ConfigurationSection inventorySection = config.getConfigurationSection("inventory");
        if (inventorySection != null) {
            for (String slotString : inventorySection.getKeys(false)) {
                int slot = Integer.parseInt(slotString);
                ItemStack item = inventorySection.getItemStack(slotString);
                inventory.setItem(slot, item);
            }
        }
        return inventory;
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


    public static void addMember(Player owner, String backpackName, Player newMember) {
        FileConfiguration config = getConfig(backpackName);
        if (config == null) {
            owner.sendMessage(Main.getPrefix() + "§cBackpack not found!");
            return;
        }
        if (newMember == null){
            owner.sendMessage(Main.getInvalidPlayer());
            return;
        }
        UUID ownerUUID = owner.getUniqueId();
        UUID newMemberUUID = newMember.getUniqueId();
        if (!ownerUUID.equals(UUID.fromString(config.getString("owner")))) {
            owner.sendMessage(Main.getPrefix() + "§cYou are not the owner of this backpack!");
            return;
        }
        List<String> members = config.getStringList("members");
        if (members.contains(newMemberUUID.toString())) {
            owner.sendMessage(Main.getPrefix() + "§cThis player is already a member of this backpack!");
            return;
        }
        members.add(newMemberUUID.toString());
        config.set("members", members);
        saveConfig(backpackName, config);
        owner.sendMessage(Main.getPrefix() + "§7The player §a" + newMember.getName() + " §7was successfully added to §a" + backpackName);
    }

    public static void removeMember(Player owner, String backpackName, Player memberToRemove) {
        FileConfiguration config = getConfig(backpackName);
        if (config == null) {
            owner.sendMessage(Main.getPrefix() + "§cBackpack not found!");
            return;
        }
        String ownerUUID = owner.getUniqueId().toString();
        if (!ownerUUID.equals(config.getString("owner"))) {
            owner.sendMessage(Main.getPrefix() + "§cYou are not the owner of this backpack!");
            return;
        }
        List<String> members = config.getStringList("members");
        String memberUUID = memberToRemove.getUniqueId().toString();
        if (!members.contains(memberUUID)) {
            owner.sendMessage(Main.getPrefix() + "§cThis player is not a member of this backpack!");
            return;
        }
        members.remove(memberUUID);
        config.set("members", members);
        saveConfig(backpackName, config);
        owner.sendMessage(Main.getPrefix() + "§7The player §a" + memberToRemove.getName() + " §7was successfully removed from §a" + backpackName);
    }


    public static boolean playerOwnsBackpack(Player player, String backpackName) {
        FileConfiguration config = getConfig(backpackName);
        if (config == null) {
            return false;
        }
        return config.getString("owner").equals(player.getUniqueId().toString());
    }

    public static boolean hasBackpacks(Player player) {
        for (String backpackName : getBackpackNames()) {
            FileConfiguration config = getConfig(backpackName);
            if (config.getString("owner").equals(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static void renameConfig(Player player, String backpackName, String newName){
        File configFile = new File(backpacksFolder, backpackName + ".yml");
        File newFile = new File(backpacksFolder, newName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        configFile.renameTo(newFile);

        player.sendMessage(Main.getPrefix() + "§a" + backpackName + " §7has been successfully renamed to §a" + newName);
    }

}
