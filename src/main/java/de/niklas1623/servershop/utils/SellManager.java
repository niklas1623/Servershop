package de.niklas1623.servershop.utils;

import de.niklas1623.servershop.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;


public class SellManager {
    public static int IDinShop;
    public static int task;


    public static void sellAll(Player player, Inventory inventory) {
        double totalSale = calculateTotalValue(inventory);
        if (totalSale <= 0) {
            player.closeInventory();
            player.sendMessage(Main.getInstance().NoItem);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
            for (int row = 1; row < 5; row++) {
                for (int col = 1; col < 8; col++) {
                    ItemStack itemStack = inventory.getItem(col + row * 9);
                    if (itemStack != null && itemStack.getType() != Material.AIR){
                        InventoryManager.returnItem(player, itemStack);
                    }
                }
            }
            return;
        }
        if (Main.getInstance().ServerAccount) {
            if (Main.getInstance().AccountName != null) {
                if (Main.econ.getBalance(Main.getInstance().AccountName) >= totalSale) {
                    Main.econ.depositPlayer(player, totalSale);
                    Main.econ.withdrawPlayer(Main.getInstance().AccountName, totalSale);
                } else {
                    player.closeInventory();
                    player.sendMessage(Main.getInstance().ServerHasNoMoney);
                    for (int row = 1; row < 5; row++) {
                        for (int col = 1; col < 8; col++) {
                            ItemStack itemStack = inventory.getItem(col + row * 9);
                            if (itemStack != null && itemStack.getType() != Material.AIR) {
                                InventoryManager.returnItem(player, itemStack);
                            }
                        }
                    }
                    return;
                }
            } else {
                player.sendMessage(Main.getInstance().NoServerShopAccount);
                Main.econ.depositPlayer(player, totalSale);
            }
        } else {
            Main.econ.depositPlayer(player, totalSale);
        }
        for (int row = 1; row < 5; row++) {
            for (int col = 1; col < 8; col++) {
                ItemStack itemStack = inventory.getItem(col + row * 9);
                if (itemStack != null) {
                    int ItemID = ShopManager.getItemID(itemStack.getType().name());
                    if (!(ItemID == 0)){
                        int IDinShop = ShopManager.getIDinShop("s", ItemID);
                        if (!(IDinShop == 0)) {
                            ItemMeta meta = itemStack.getItemMeta();
                            if (!meta.hasEnchants() && !meta.hasLore() && !meta.hasAttributeModifiers() && !meta.hasCustomModelData() && !((Damageable) meta).hasDamage()) {
                                if (ShopManager.getPrice(IDinShop) != 0) {
                                    inventory.setItem(col + row * 9, null);
                                }
                            }
                        }
                    }
                }

            }
        }
        player.sendMessage(Main.getInstance().ItemSell.replaceAll("%price%", Main.econ.format(totalSale)+""));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 100, 1);
        player.closeInventory();
        Bukkit.getScheduler().cancelTask(task);
        for (int row = 1; row < 5; row++) {
            for (int col = 1; col < 8; col++) {
                ItemStack itemStack = inventory.getItem(col + row * 9);
                if (itemStack != null && itemStack.getType() != Material.AIR){
                    InventoryManager.returnItem(player, itemStack);
                }
            }
        }
    }


    public static double calculateTotalValue(Inventory inventory) {
        double total = 0.0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null) {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    short maxDurability = itemStack.getType().getMaxDurability();
                    if (!meta.hasEnchants() && !meta.hasLore() && !meta.hasAttributeModifiers() && !meta.hasCustomModelData() && !((Damageable) meta).hasDamage()) {
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
            }
        }
        return total;
    }

}
