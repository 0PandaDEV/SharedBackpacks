package net.pandadev.sharedbackpacks.utils;

import net.pandadev.sharedbackpacks.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class BackpackAPI {

    private static final Map<String, Inventory> backpacks = new HashMap<>();

    public static void createBackpack(Player player, String backpackName) {
        Inventory inventory = Bukkit.createInventory(player, 54, backpackName);
        backpacks.put(backpackName, inventory);

        FileConfiguration config = ConfigManager.createConfig(backpackName);
        config.set("owner", player.getUniqueId().toString());
        config.set("members", new ArrayList<UUID>());
        config.set("inventory", serializeInventory(inventory));
        ConfigManager.saveConfig(backpackName, config);
    }

    public static void openBackpackGui(Player player, String backpackName) {
        Inventory inventory = backpacks.get(backpackName);
        FileConfiguration config = ConfigManager.loadConfig(backpackName);
        if (inventory == null) {

            UUID ownerUUID = UUID.fromString(Objects.requireNonNull(config.getString("owner")));
            List<String> members = config.getStringList("members");
            if (!ownerUUID.equals(player.getUniqueId()) && !members.contains(player.getUniqueId().toString())) {
                player.sendMessage(Main.getPrefix() + "§cYou are not the owner or a member of this backpack!");
                return;
            }

            String inventoryString = config.getString("inventory");
            assert inventoryString != null;
            inventory = deserializeInventory(inventoryString, backpackName);

            backpacks.put(backpackName, inventory);
        }

        UUID ownerUUID = UUID.fromString(Objects.requireNonNull(config.getString("owner")));
        List<String> members = config.getStringList("members");
        if (!ownerUUID.equals(player.getUniqueId()) && !members.contains(player.getUniqueId().toString())) {
            player.sendMessage(Main.getPrefix() + "§cYou are not the owner or a member of this backpack!");
            return;
        }

        player.openInventory(inventory);
    }

    public static String serializeInventory(Inventory inventory) {
        StringBuilder sb = new StringBuilder();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                sb.append(item.getType().name()).append(":").append(item.getAmount()).append(",");
            } else {
                sb.append("AIR:0,");
            }
        }
        return sb.toString();
    }

    public static Inventory deserializeInventory(String data, String backpackName) {
        String[] itemData = data.split(",");
        Inventory inventory = Bukkit.createInventory(null, 54, backpackName);
        for (int i = 0; i < itemData.length; i++) {
            String[] typeAndAmount = itemData[i].split(":");
            Material material = Material.valueOf(typeAndAmount[0]);
            int amount = Integer.parseInt(typeAndAmount[1]);
            inventory.setItem(i, new ItemStack(material, amount));
        }
        return inventory;
    }

    public static void saveBackpack(String backpackName) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                InventoryView openInventoryView = player.getOpenInventory();
                if (openInventoryView.getTitle().equals(backpackName)) {
                    return;
                }
            }

            Inventory inventory = backpacks.get(backpackName);
            if (inventory != null) {
                String serializedInventory = serializeInventory(inventory);
                FileConfiguration config = ConfigManager.loadConfig(backpackName);
                config.set("inventory", serializedInventory);
                ConfigManager.saveConfig(backpackName, config);
            }

            backpacks.remove(backpackName);
        }, 1L);
    }

    public static void addMember(Player player, String backpackName, Player member) {
        FileConfiguration config = ConfigManager.loadConfig(backpackName);

        UUID ownerUUID = UUID.fromString(Objects.requireNonNull(config.getString("owner")));
        if (!ownerUUID.equals(player.getUniqueId())) {
            player.sendMessage(Main.getPrefix() + "§cYou are not the owner of this backpack!");
            return;
        }

        List<String> members = config.getStringList("members");
        String newMemberUUID = member.getUniqueId().toString();

        if (members.contains(newMemberUUID)) {
            player.sendMessage(Main.getPrefix() + "§cThis player is already a member of the backpack!");
            return;
        }

        members.add(newMemberUUID);
        config.set("members", members);
        ConfigManager.saveConfig(backpackName, config);
        player.sendMessage(Main.getPrefix() + "§7The player §a" + member.getName() + " §7was successfully added to §a" + backpackName);

        // Print out the members list
        System.out.println("Members after adding: " + members);
    }

    public static void removeMember(Player player, String backpackName, Player member) {
        FileConfiguration config = ConfigManager.loadConfig(backpackName);
        UUID ownerUUID = UUID.fromString(config.getString("owner"));
        if (!ownerUUID.equals(player.getUniqueId())) {
            player.sendMessage(Main.getPrefix() + "§cYou are not the owner of this backpack!");
            return;
        }
        List<String> members = config.getStringList("members");
        UUID memberUUID = member.getUniqueId();
        if (!members.contains(memberUUID.toString())) {
            player.sendMessage(Main.getPrefix() + "§cThis player is not a member of the backpack!");
            return;
        }
        members.remove(memberUUID.toString());
        config.set("members", members);
        ConfigManager.saveConfig(backpackName, config);
        player.sendMessage(Main.getPrefix() + "§7The player §a" + member.getName() + " §7was successfully removed from §a" + backpackName);
    }

    public static boolean hasBackpack(Player player) {
        List<String> backpackNames = getBackpackNames();
        for (String backpackName : backpackNames) {
            FileConfiguration config = ConfigManager.loadConfig(backpackName);
            if (config != null) {
                UUID ownerUUID = UUID.fromString(config.getString("owner"));
                List<String> members = config.getStringList("members");
                if (ownerUUID.equals(player.getUniqueId()) || members.contains(player.getUniqueId().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> getBackpackNames() {
        File backpacksFolder = new File("plugins/SharedBackpacks/backpacks");
        File[] files = backpacksFolder.listFiles();
        List<String> backpackNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".yml") && !fileName.startsWith("._")) {
                    String backpackName = fileName.replace(".yml", "");
                    backpackNames.add(backpackName);
                }
            }
        }
        return backpackNames;
    }

    public static List<String> getOwnedBackpackNames(Player player) {
        File backpacksFolder = new File("plugins/SharedBackpacks/backpacks");
        File[] files = backpacksFolder.listFiles();
        List<String> ownedBackpackNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".yml") && !fileName.startsWith("._")) {
                    String backpackName = fileName.replace(".yml", "");
                    FileConfiguration config = ConfigManager.loadConfig(backpackName);
                    UUID ownerUUID = UUID.fromString(Objects.requireNonNull(config.getString("owner")));
                    if (ownerUUID.equals(player.getUniqueId())) {
                        ownedBackpackNames.add(backpackName);
                    }
                }
            }
        }
        return ownedBackpackNames;
    }

    public static List<String> getAccessibleBackpackNames(Player player) {
        File backpacksFolder = new File("plugins/SharedBackpacks/backpacks");
        File[] files = backpacksFolder.listFiles();
        List<String> accessibleBackpackNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".yml") && !fileName.startsWith("._")) {
                    String backpackName = fileName.replace(".yml", "");
                    FileConfiguration config = ConfigManager.loadConfig(backpackName);
                    String owner = config.getString("owner");
                    if (owner != null) {
                        UUID ownerUUID = UUID.fromString(owner);
                        List<String> members = config.getStringList("members");
                        if (ownerUUID.equals(player.getUniqueId()) || members.contains(player.getUniqueId().toString())) {
                            accessibleBackpackNames.add(backpackName);
                        }
                    }
                }
            }
        }
        return accessibleBackpackNames;
    }

    public static List<UUID> getAllMembers(String backpackName) {
        FileConfiguration config = ConfigManager.loadConfig(backpackName);
        List<String> memberUUIDs = config.getStringList("members");

        List<UUID> members = new ArrayList<>();
        for (String uuidString : memberUUIDs) {
            UUID uuid = UUID.fromString(uuidString);
            members.add(uuid);
        }

        return members;
    }

}
