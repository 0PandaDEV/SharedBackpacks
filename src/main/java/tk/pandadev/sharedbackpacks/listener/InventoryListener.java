package tk.pandadev.sharedbackpacks.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import tk.pandadev.sharedbackpacks.utils.BackpackAPI;

import java.util.List;
import java.util.UUID;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        InventoryView inventoryView = event.getView();
        String inventoryTitle = inventoryView.getTitle();
        String backpackName = null;
        for (String name : BackpackAPI.getBackpackNames()) {
            if (inventoryTitle.trim().equalsIgnoreCase(name.trim())) {
                backpackName = name;
                break;
            }
        }
        if (backpackName == null) {
            return;
        }
        FileConfiguration config = BackpackAPI.getConfig(backpackName);
        if (config == null) {
            return;
        }
        List<String> members = config.getStringList("members");
        if (!player.getUniqueId().equals(UUID.fromString(config.getString("owner"))) && !members.contains(player.getUniqueId().toString())) {
            return;
        }
        BackpackAPI.inventorys.put(backpackName, inventory.getContents());
        BackpackAPI.saveData(player, backpackName, members, inventory.getContents());
    }

//    @EventHandler
//    public void onItemMove(InventoryMoveItemEvent event){
//        Player player = (Player) event.;
//        Inventory inventory = event.getDestination();
//        InventoryView inventoryView = event.getDestination();
//        String inventoryTitle = inventoryView.getTitle();
//        String backpackName = null;
//        for (String name : BackpackAPI.getBackpackNames()) {
//            if (inventoryTitle.trim().equalsIgnoreCase(name.trim())) {
//                backpackName = name;
//                break;
//            }
//        }
//        if (backpackName == null) {
//            return;
//        }
//        FileConfiguration config = BackpackAPI.getConfig(backpackName);
//        if (config == null) {
//            return;
//        }
//        List<String> members = config.getStringList("members");
//        if (!player.getUniqueId().equals(UUID.fromString(config.getString("owner"))) && !members.contains(player.getUniqueId().toString())) {
//            return;
//        }
//        BackpackAPI.inventorys.put(backpackName, inventory.getContents());
//    }


}
