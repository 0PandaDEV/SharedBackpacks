package tk.pandadev.sharedbackpacks;

import games.negative.framework.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tk.pandadev.sharedbackpacks.commands.BackpacksCommand;
import tk.pandadev.sharedbackpacks.commands.BagCommand;
import tk.pandadev.sharedbackpacks.listener.InventoryCloseListener;
import tk.pandadev.sharedbackpacks.utils.BackpackAPI;

import java.util.List;
import java.util.UUID;

public final class Main extends BasePlugin {

    private static final String prefix = "§x§0§0§7§3§f§f§lBackpacks §8» ";
    private static final String noPerm = prefix + "§cYou do not have permission to do this";
    private static final String invalidPlayer = prefix + "§cThis player is not online";
    private static final String noBackpack = prefix + "§cYou don't have a backpack yet create one with §6/backpack create §cor ask one of the other players to add you to an existing one.";
    private static Main instance;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        saveDefaultConfig();

        Bukkit.getConsoleSender().sendMessage(prefix + "§aEnabled");
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getCommand("backpacks").setExecutor(new BackpacksCommand());
        getCommand("backpack").setExecutor(new BagCommand());
    }

    @Override
    public void onDisable() {
        instance = null;
        Bukkit.getConsoleSender().sendMessage(prefix + "§cDisabled");
    }

    public static void openBackpack(Player player, String backpackName) {
        FileConfiguration config = BackpackAPI.getConfig(backpackName);
        if (config == null) {
            player.sendMessage(prefix + "§cBackpack not found!");
        } else {
            UUID ownerUUID = UUID.fromString(config.getString("owner"));
            if (ownerUUID.equals(player.getUniqueId())) {
                player.openInventory(BackpackAPI.loadInventory(config, backpackName));
            } else {
                List<String> members = config.getStringList("members");
                if (!members.contains(player.getName())) {
                    player.sendMessage(prefix + "§7You are not a member of this backpack!");
                }
                player.openInventory(BackpackAPI.loadInventory(config, backpackName));
            }
        }
    }



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
