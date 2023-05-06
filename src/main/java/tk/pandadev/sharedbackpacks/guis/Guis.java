package tk.pandadev.sharedbackpacks.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tk.pandadev.sharedbackpacks.Main;
import tk.pandadev.sharedbackpacks.utils.BackpackAPI;
import tk.pandadev.sharedbackpacks.utils.CustomHead;

import java.util.Collections;

public class Guis {
    public static void backpackConfig(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Backpack Config"))
                .rows(5)
                .disableAllInteractions()
                .create();

        gui.setItem(3, 3, ItemBuilder.from(Material.NAME_TAG).name(Component.text("§x§e§3§c§5§8§aRename Backpack")).asGuiItem(event -> {
            renameBackpack(player);
        }));
        gui.setItem(3, 7, ItemBuilder.from(Material.LIME_DYE).name(Component.text("§aCreate Backpack")).asGuiItem(event -> {
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {

                        BackpackAPI.createConfig(player, completion.getText());

                        player.sendMessage(Main.getPrefix() + "§7Backpack named §a" + completion.getText() + " §7was successfully created");

                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    })
                    .preventClose()
                    .itemLeft(new ItemStack(Material.NAME_TAG))
                    .title("Enter the name")
                    .plugin(Main.getInstance())
                    .open(player);
            gui.open(player);
        }));

        gui.setItem(2, 5, ItemBuilder.from(Material.RED_DYE).name(Component.text("§cDelete Backpack")).asGuiItem(event -> {
            deleteBackpack(player);
        }));

        gui.open(player);
    }

    public static void renameBackpack(Player player){
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Pick a Backpack for renaming"))
                .rows(3)
                .disableAllInteractions()
                .create();

        for (String backpack : BackpackAPI.getBackpackNames()){
            GuiItem item = ItemBuilder.from(Material.BARREL).name(Component.text(backpack)).asGuiItem(event -> {
                
                ItemStack leftItem = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = leftItem.getItemMeta();
                meta.setDisplayName(backpack);
                leftItem.setItemMeta(meta);
                
                new AnvilGUI.Builder()
                        .onComplete((completion) -> {

                            BackpackAPI.renameConfig(player, backpack, completion.getText());

                            Main.refreshMap();

                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        })
                        .preventClose()
                        .itemLeft(leftItem)
                        .title("Enter the name")
                        .text(backpack)
                        .plugin(Main.getInstance())
                        .open(player);
                
            });
            gui.addItem(item);
        }

        gui.setItem(3, 1, ItemBuilder.from(Material.PAPER).setName("Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(3, 9, ItemBuilder.from(Material.PAPER).setName("Next").asGuiItem(event -> gui.next()));

        gui.getFiller().fillBottom(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());

        gui.open(player);
    }

    public static void deleteBackpack(Player player){
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Pick a Backpack to delete"))
                .rows(3)
                .disableAllInteractions()
                .create();

        for (String backpack : BackpackAPI.getBackpackNames()){
            gui.addItem(ItemBuilder.from(Material.BARREL).name(Component.text(backpack)).asGuiItem(event -> {

                Gui confirm = Gui.gui()
                        .title(Component.text("Confirm your deletion"))
                        .rows(3)
                        .disableAllInteractions()
                        .create();

                confirm.setItem(2, 4, ItemBuilder.skull(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0=")).name(Component.text("§aConfirm")).asGuiItem(event1 -> {
                    BackpackAPI.deleteConfig(player, backpack);
                    player.closeInventory();
                }));

                confirm.setItem(2, 6, ItemBuilder.skull(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==")).name(Component.text("§cCancel")).asGuiItem(event1 -> {
                    player.closeInventory();
                }));

                confirm.open(player);

            }));
        }

        gui.open(player);
    }

}
