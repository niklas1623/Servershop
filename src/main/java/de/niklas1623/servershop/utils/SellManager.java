package de.niklas1623.servershop.utils;

import de.niklas1623.servershop.Main;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.crypto.spec.IvParameterSpec;

public class SellManager {
    public static int IDinShop;
    public static int task;


    public static void sellAll(Player player) {
        double totalSale = calculateTotalValue(InventoryManager.sellShop.getInventory());
        if (totalSale <= 0) {
            player.closeInventory();
            for (int row = 1; row < 5; row++) {
                for (int col = 1; col < 8; col++) {
                    ItemStack itemStack = InventoryManager.sellShop.getInventory().getItem(col + row * 9);
                    if (itemStack != null && itemStack.getType() != Material.AIR){
                        InventoryManager.returnItem(player, itemStack);
                    }
                }
            }
            return;
        }
        Main.econ.depositPlayer(player, totalSale);
        for (int row = 1; row < 5; row++) {
            for (int col = 1; col < 8; col++) {
                ItemStack itemStack = InventoryManager.sellShop.getInventory().getItem(col + row * 9);
                if (itemStack != null) {
                    int ItemID = ShopManager.getItemID(itemStack.getType().name());
                    if (!(ItemID == 0)){
                        int IDinShop = ShopManager.getIDinShop("s", ItemID);
                        if (!(IDinShop == 0)) {
                            if (ShopManager.getPrice(IDinShop) != 0) {
                            InventoryManager.sellShop.getInventory().setItem(col + row * 9, null);
                            }
                        }
                    }
                }

            }
        }
        player.sendMessage(Main.getInstance().ItemSell.replaceAll("%price%", Main.econ.format(totalSale)+""));
        player.closeInventory();
        Bukkit.getScheduler().cancelTask(SellManager.task);
        for (int row = 1; row < 5; row++) {
            for (int col = 1; col < 8; col++) {
                ItemStack itemStack = InventoryManager.sellShop.getInventory().getItem(col + row * 9);
                if (itemStack != null && itemStack.getType() != Material.AIR){
                    InventoryManager.returnItem(player, itemStack);
                }
            }
        }
    }


    public static double calculateTotalValue(Inventory inventory) {
        double total = 0.0;
        for (ItemStack itemStack : InventoryManager.sellShop.getInventory().getContents()) {
            if (itemStack != null) {
                int ItemID = ShopManager.getItemID(itemStack.getType().name());
                IDinShop = ShopManager.getIDinShop("s", ItemID);
                if (!(IDinShop == 0)) {
                    int amount = ShopManager.getAmount(IDinShop);
                    double price = ShopManager.getPrice(IDinShop);
                    double price_for_one = price / amount;

                    if (price_for_one != 0)
                        total = total + (price_for_one * itemStack.getAmount());
                }
            }
        }
        return total;
    }

    public static void updateSell() {
        Inventory inventory = InventoryManager.sellShop.getInventory();
        double totalSale = calculateTotalValue(inventory);
        InventoryManager.sellShop.updateItem(6 , 5, ItemBuilder.from(Material.valueOf(Main.getInstance().SellItem)).setName(Main.getInstance().SellName.replaceAll("%price%", Main.econ.format(totalSale)+"")).setLore(Main.getInstance().SellDesc).asGuiItem(event -> {
            event.setCancelled(true);
            sellAll((Player) event.getWhoClicked());
        }));

    }

    public static void runTask() {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), SellManager::updateSell, 7, 7);
    }


}
