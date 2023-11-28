package net.pandadev.sharedbackpacks.listener;

import net.pandadev.sharedbackpacks.utils.BackpackAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryView inventoryView = event.getView();
        BackpackAPI.saveBackpack(inventoryView.getTitle());
    }
}
