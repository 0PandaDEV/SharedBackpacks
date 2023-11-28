package net.pandadev.sharedbackpacks;

import net.pandadev.sharedbackpacks.commands.BackpacksCommand;
import net.pandadev.sharedbackpacks.commands.BagCommand;
import net.pandadev.sharedbackpacks.listener.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static final String prefix = "§x§0§0§7§3§f§f§lBackpacks §8» ";
    private static final String noPerm = prefix + "§cYou do not have permission to do this";
    private static final String invalidPlayer = prefix + "§cThis player is not online";
    private static final String noBackpack = prefix + "§cYou don't have a backpack yet create one with §6/backpack create §cor ask one of the other players to add you to an existing one.";
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        //saveDefaultConfig();

        Bukkit.getConsoleSender().sendMessage(prefix + "§aEnabled");
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        getCommand("backpacks").setExecutor(new BackpacksCommand());
        getCommand("backpack").setExecutor(new BagCommand());
//        refreshMap();
    }

    @Override
    public void onDisable() {
        instance = null;
        Bukkit.getConsoleSender().sendMessage(prefix + "§cDisabled");
    }

//    public static void openBackpack(Player player, String backpackName) {
//        FileConfiguration config = BackpackAPI.getConfig(backpackName);
//        if (config == null) {
//            player.sendMessage(prefix + "§cBackpack not found!");
//        } else {
//
//            InventoryHolder holder = Bukkit.getPlayer(config.getString("owner"));
//
//            Inventory inventory = Bukkit.createInventory(holder, 54, backpackName);
//            inventory.setContents(BackpackAPI.inventorys.get(backpackName));
//
//            UUID ownerUUID = UUID.fromString(config.getString("owner"));
//            if (ownerUUID.equals(player.getUniqueId())) {
//                player.closeInventory();
//                player.openInventory(inventory);
//            } else {
//                List<String> members = config.getStringList("members");
//                if (!members.contains(player.getUniqueId().toString())) {
//                    player.sendMessage(prefix + "§cYou are not a member of this backpack!");
//                    return;
//                }
//                player.closeInventory();
//                player.openInventory(inventory);
//            }
//        }
//    }

//    public static void refreshMap() {
//        for (String backpack : BackpackAPI.getBackpackNames()) {
//            if (BackpackAPI.getConfig(backpack).getList("inventory") == null) {
//                Inventory inventory = Bukkit.createInventory(null, 54, backpack);
//                BackpackAPI.inventorys.put(backpack, inventory.getContents());
//            } else {
//                Inventory inventory = Bukkit.createInventory(null, 54, backpack);
//                ConfigurationSection inventorySection = BackpackAPI.getConfig(backpack).getConfigurationSection("inventory");
//                if (inventorySection != null) {
//                    for (String slotString : inventorySection.getKeys(false)) {
//                        int slot = Integer.parseInt(slotString);
//                        ItemStack item = inventorySection.getItemStack(slotString);
//                        inventory.setItem(slot, item);
//                    }
//                }
//
//                BackpackAPI.inventorys.put(backpack, inventory.getContents());
//            }
//        }
//    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getInvalidPlayer() {
        return invalidPlayer;
    }

    public static String getNoBackpack() {
        return noBackpack;
    }

    public static String getNoPerm() {
        return noPerm;
    }

    public static Main getInstance() {
        return instance;
    }
}
