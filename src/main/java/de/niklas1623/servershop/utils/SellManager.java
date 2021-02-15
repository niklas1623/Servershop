package de.niklas1623.servershop.utils;

import de.niklas1623.servershop.Main;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellManager {
    public static int IDinShop;
    public static int task;


    public static void sellAll(Player player) {
        double totalSale = calculateTotalValue(InventoryManager.sellShop.getInventory());
        if (totalSale <= 0.0) {
            player.closeInventory();
            return;
        }
        Main.econ.depositPlayer(player, totalSale);
        for (int row = 1; row < 5; row++) {
            for (int col = 1; col < 8; col++) {
                ItemStack itemStack = InventoryManager.sellShop.getInventory().getItem(col + row * 9);
                if (itemStack != null && ShopManager.getPrice(IDinShop) != 0)
                    InventoryManager.sellShop.getInventory().setItem(col + row * 9, null);
            }
        }
        player.closeInventory();
    }


    public static double calculateTotalValue(Inventory inventory) {
        double total = 0;
        for (ItemStack itemStack : InventoryManager.sellShop.getInventory().getContents()) {
            //int ItemID = ShopManager.getItemID(itemStack.getType().name());
            int ItemID = 2;
            IDinShop = ShopManager.getIDinShop("s", ItemID);
            int amount = ShopManager.getAmount(IDinShop);
            double price = ShopManager.getPrice(IDinShop);
            double price_for_one = price/amount;
            int cal_amount = itemStack.getAmount();


            if (price_for_one != 0)
                total = total + (price_for_one*cal_amount);
        }
        return total;
    }

    public static void updateSell() {
        double totalSale = calculateTotalValue(InventoryManager.sellShop.getInventory());
        Inventory inventory = InventoryManager.sellShop.getInventory();
        InventoryManager.sellShop.updateItem(6 , 5, ItemBuilder.from(Material.valueOf(Main.getInstance().SellItem)).setName(Main.getInstance().SellName.replaceAll("%price%", Main.getInstance().econ.format(totalSale)+"")).setLore(Main.getInstance().SellDesc).asGuiItem(event -> {
            event.setCancelled(true);
        }));

    }

    public static void runTask() {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), SellManager::updateSell, 7, 7);
    }


}
