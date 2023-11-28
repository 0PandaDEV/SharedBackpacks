package net.pandadev.sharedbackpacks.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.pandadev.sharedbackpacks.Main;
import net.pandadev.sharedbackpacks.utils.BackpackAPI;
import net.pandadev.sharedbackpacks.utils.CustomHead;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Guis {
    public static void backpackConfig(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Backpack Config"))
                .rows(5)
                .disableAllInteractions()
                .create();

//        gui.setItem(3, 3, ItemBuilder.from(Material.NAME_TAG).name(Component.text("§x§e§3§c§5§8§aRename Backpack")).asGuiItem(event -> {
//            renameBackpack(player);
//        }));

        gui.setItem(3, 7, ItemBuilder.from(Material.LIME_DYE).name(Component.text("§aCreate Backpack")).asGuiItem(event -> {
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
            gui.open(player);
        }));

//        gui.setItem(2, 5, ItemBuilder.from(Material.RED_DYE).name(Component.text("§cDelete Backpack")).asGuiItem(event -> {
//            deleteBackpack(player);
//        }));

        gui.setItem(4, 5, ItemBuilder.from(Material.PLAYER_HEAD).name(Component.text("§x§5§c§3§c§2§8Manage Members")).asGuiItem(event -> {
            manageMembers(player);
        }));

        gui.open(player);
    }

//    public static void renameBackpack(Player player){
//        PaginatedGui gui = Gui.paginated()
//                .title(Component.text("Pick a Backpack for renaming"))
//                .rows(3)
//                .disableAllInteractions()
//                .create();
//
//        for (String backpack : BackpackAPI.getBackpackNames()){
//            GuiItem item = ItemBuilder.from(Material.BARREL).name(Component.text(backpack)).asGuiItem(event -> {
//
//                ItemStack leftItem = new ItemStack(Material.NAME_TAG);
//                ItemMeta meta = leftItem.getItemMeta();
//                meta.setDisplayName(backpack);
//                leftItem.setItemMeta(meta);
//
//                new AnvilGUI.Builder()
//                        .onClick((state, text) -> {
//
//                            BackpackAPI.renameConfig(player, backpack, text.getText());
//
//                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
//                        })
//                        .preventClose()
//                        .itemLeft(leftItem)
//                        .title("Enter the name")
//                        .text(backpack)
//                        .plugin(Main.getInstance())
//                        .open(player);
//
//            });
//            gui.addItem(item);
//        }
//
//        gui.setItem(3, 1, ItemBuilder.from(Material.PAPER).setName("§fPrevious").asGuiItem(event -> gui.previous()));
//        gui.setItem(3, 9, ItemBuilder.from(Material.PAPER).setName("f§Next").asGuiItem(event -> gui.next()));
//
//        gui.getFiller().fillBottom(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
//
//        gui.open(player);
//    }

//    public static void deleteBackpack(Player player){
//        PaginatedGui gui = Gui.paginated()
//                .title(Component.text("Pick a Backpack to delete"))
//                .rows(3)
//                .disableAllInteractions()
//                .create();
//
//        for (String backpack : BackpackAPI.getBackpackNames()){
//            gui.addItem(ItemBuilder.from(Material.BARREL).name(Component.text("§f" + backpack)).asGuiItem(event -> {
//
//                Gui confirm = Gui.gui()
//                        .title(Component.text("Confirm your deletion"))
//                        .rows(3)
//                        .disableAllInteractions()
//                        .create();
//
//                confirm.setItem(2, 4, ItemBuilder.skull(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0=")).name(Component.text("§aConfirm")).asGuiItem(event1 -> {
//                    BackpackAPI.deleteConfig(player, backpack);
//                    player.closeInventory();
//                }));
//
//                confirm.setItem(2, 6, ItemBuilder.skull(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==")).name(Component.text("§cCancel")).asGuiItem(event1 -> {
//                    player.closeInventory();
//                }));
//
//                confirm.open(player);
//
//            }));
//        }
//
//        gui.open(player);
//    }

    public static void manageMembers(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Pick a Backpack to manage"))
                .rows(3)
                .disableAllInteractions()
                .create();

        List<String> players = new ArrayList<>();
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            players.add(player1.getName());
        }
        players.remove(player.getName());

        for (String backpackName : BackpackAPI.getBackpackNames()) {
            gui.addItem(ItemBuilder.from(Material.BARREL).name(Component.text("§f" + backpackName)).asGuiItem(event -> {
                manageMembersGUI(player, players, backpackName);
            }));
        }


        gui.setItem(3, 1, ItemBuilder.from(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")).setName("§fPrevious").asGuiItem(event -> gui.previous()));
        gui.setItem(3, 9, ItemBuilder.from(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")).setName("§fNext").asGuiItem(event -> gui.next()));
        gui.open(player);
    }

    public static void manageMembersGUI(Player player, List<String> players, String backpackName) {
        PaginatedGui innerGUI = Gui.paginated()
                .title(Component.text("Click the players to add/remove"))
                .rows(5)
                .disableAllInteractions()
                .create();

        for (String player1 : players) {
            Player castPlayer = Bukkit.getPlayer(player1);

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            assert meta != null;
            meta.setOwner(player1);
            skull.setItemMeta(meta);

            innerGUI.addItem(ItemBuilder.skull(skull).name(Component.text("§f" + player1)).addLore(BackpackAPI.getAllMembers(backpackName).contains(castPlayer.getUniqueId()) ? "§aHas access" : "§cHas No access").asGuiItem(event1 -> {
                if (BackpackAPI.getAllMembers(backpackName).contains(castPlayer.getUniqueId())) {
                    BackpackAPI.removeMember(player, backpackName, castPlayer);
                    player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 100, 0.5f);
                } else {
                    BackpackAPI.addMember(player, backpackName, castPlayer);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);
                }
                manageMembersGUI(player, players, backpackName);
            }));
        }

        innerGUI.setItem(5, 1, ItemBuilder.from(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")).setName("§fPrevious").asGuiItem(event1 -> innerGUI.previous()));
        innerGUI.setItem(5, 9, ItemBuilder.from(CustomHead.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")).setName("§fNext").asGuiItem(event1 -> innerGUI.next()));

        innerGUI.open(player);
    }

}
