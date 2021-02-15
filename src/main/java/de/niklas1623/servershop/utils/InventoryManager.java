package de.niklas1623.servershop.utils;

import de.niklas1623.servershop.commands.ShopCommands;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import me.mattstudios.mfgui.gui.guis.PersistentGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.niklas1623.servershop.Main;
import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryManager implements Listener {
    public static Main pl = Main.getInstance();
    public static String material;
    public static double price;
    public static int amount;
    public static int Category;

    public static Gui menuItem = new Gui(6,  Main.getInstance().ServershopName);
    public static Gui sellShop = new Gui(6, pl.ItemSellName);


    public static void openServershop(Player player) {
        Gui gui = new Gui(3, Main.getInstance().ServershopName);
        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        GuiItem buy = ItemBuilder.from(Material.CHEST).setName(pl.ItemBuyName).setLore(pl.ItemBuyDesc).asGuiItem(event -> {
            if(!event.getClick().isLeftClick()){
            }
            selectMenu(player);
        });
        GuiItem sell = ItemBuilder.from(Material.CHEST).setName(pl.ItemSellName).setLore(pl.ItemSellDesc).asGuiItem(event -> {
            //event.setCancelled(true);
            //player.closeInventory();
            //player.sendMessage(Main.getInstance().ComingSoon.replaceAll("%prefix%", Main.getInstance().prefix));
            openSellShop(player);
        });

        GuiItem money = ItemBuilder.from(Material.valueOf(pl.CurrentMoneyItem)).setName(pl.CurrentMoney).setLore("§e"+Main.econ.format(Main.econ.getBalance(player))).asGuiItem();
        ArrayList<String> info = new ArrayList<>();
        info.add("");
        info.add("§7Made by:§o niklas1623");
        info.add("§7Version: §o"+Main.getInstance().getDescription().getVersion());

        GuiItem pluginInfo = ItemBuilder.from(Material.COMMAND_BLOCK).setName("§a§l"+Main.getInstance().getDescription().getName()).setLore(info).asGuiItem();

        gui.setItem(4, pluginInfo);
        gui.setItem(22, money);
        gui.setItem(11, buy);
        gui.setItem(15, sell);
        gui.open(player);
    }

    public static void selectMenu(Player player){
        menuItem.getFiller().fillBorder(ItemBuilder.from(Material.valueOf(pl.BorderItem)).setName(" ").asGuiItem());
        menuItem.getFiller().fill(ItemBuilder.from(Material.valueOf(pl.FillingItem)).setName(" ").asGuiItem());
        menuItem.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        MenuManager.loadShopMenu();
        GuiItem money = ItemBuilder.from(Material.valueOf(pl.CurrentMoneyItem)).setName(pl.CurrentMoney).setLore("§e"+Main.econ.format(Main.econ.getBalance(player))).asGuiItem();
        GuiItem back = ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            openServershop(player);
        });
        menuItem.setItem(45,back);
        menuItem.setItem(49,money);
        menuItem.open(player);
    }

    public static void selectBuyItem(Player player){
        PaginatedGui shopItem = new PaginatedGui(3,18, MenuManager.Category_Name);

        shopItem.getFiller().fillBottom(ItemBuilder.from(Material.valueOf(pl.BottomItem)).setName(" ").asGuiItem());
        shopItem.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        shopItem.setItem(3,3, ItemBuilder.from(Material.valueOf(pl.PreviousPageItem)).setName(pl.PreviousPage).asGuiItem(event -> shopItem.previous()));
        shopItem.setItem(3,7, ItemBuilder.from(Material.valueOf(pl.NextPageItem)).setName(pl.NextPage).asGuiItem(event -> shopItem.next()));
        shopItem.setItem(3,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            selectMenu(player);
        }));
        List<Integer> itemlist = ShopManager.getItems(MenuManager.ClickedCategory, "b");
        if (!(itemlist.size() == 0)) {
            for (int i = 0; i < itemlist.size(); i++) {
                amount = ShopManager.getAmount(itemlist.get(i));
                price = ShopManager.getPrice(itemlist.get(i));
                material = ShopManager.getMaterial(itemlist.get(i));
                shopItem.addItem(ItemBuilder.from(Material.valueOf(material.toUpperCase())).setAmount(amount).setLore("§7" + Main.econ.format(price)).asGuiItem(event -> {
                    openBuyShop(player);
                }));
            }
            shopItem.open(player);

        } else {
            player.closeInventory();
            player.sendMessage(pl.NotAvailable);
        }
        itemlist.clear();
    }

    public static void selectSearchItem(Player player){
        PaginatedGui shopItem = new PaginatedGui(3,18, "§b§o§l"+ShopCommands.input.replaceAll("%", ""));

        shopItem.getFiller().fillBottom(ItemBuilder.from(Material.valueOf(pl.BottomItem)).setName(" ").asGuiItem());
        shopItem.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        shopItem.setItem(3,3, ItemBuilder.from(Material.valueOf(pl.PreviousPageItem)).setName(pl.PreviousPage).asGuiItem(event -> shopItem.previous()));
        shopItem.setItem(3,7, ItemBuilder.from(Material.valueOf(pl.NextPageItem)).setName(pl.NextPage).asGuiItem(event -> shopItem.next()));
        shopItem.setItem(3,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            selectMenu(player);
        }));
        List<Integer> list = ShopManager.getSearchItem(ShopCommands.input);
        if (!(list.size() == 0)) {
            for (int i = 0; i < list.size(); i++) {
                int IDinShop = ShopManager.getIDinShop("b",list.get(i));
                    amount = ShopManager.getAmount(IDinShop);
                    price = ShopManager.getPrice(IDinShop);
                    material = ShopManager.getMaterial(IDinShop);
                    if (!(IDinShop == 0)){
                        shopItem.addItem(ItemBuilder.from(Material.valueOf(material)).setAmount(amount).setLore("§7" + Main.econ.format(price)).asGuiItem(event -> {
                            openBuyShop(player);
                        }));
                    }
            }
            shopItem.open(player);
            if (shopItem.getInventory().firstEmpty() == 0) {
                player.closeInventory();
                player.sendMessage(pl.NotFound.replaceAll("%input%", ShopCommands.input.replaceAll("%", "")));
            }
        } else {
            player.sendMessage(pl.NotFound.replaceAll("%input%", ShopCommands.input.replaceAll("%", "")));
        }
        list.clear();
    }

    public static void openSellShop(Player player) {
        sellShop.getFiller().fillBorder(ItemBuilder.from(Material.valueOf(pl.BorderItem)).setName(" ").asGuiItem(event -> {
            event.setCancelled(true);
        }));
        sellShop.setItem(6,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            event.setCancelled(true);
            openServershop(player);
        }));
        price = 0;
        sellShop.setItem(6 , 5, ItemBuilder.from(Material.valueOf(pl.SellItem)).setName(pl.SellName.replaceAll("%price%", pl.econ.format(price)+"")).setLore(pl.SellDesc).asGuiItem(event -> {
            event.setCancelled(true);
        }));




        sellShop.setOpenGuiAction(event -> {
            SellManager.runTask();
        });
        sellShop.open(player);
        sellShop.setCloseGuiAction(event -> {

        });
    }

    public static void returnItem(Player player, ItemStack itemStack) {
        if (itemStack == null){
            return;
        }
        Map<Integer, ItemStack> overfilled = player.getInventory().addItem(itemStack);
        if (!overfilled.isEmpty()){
            overfilled.values().forEach(item2 -> player.getWorld().dropItemNaturally(player.getLocation(), item2));
        }
    }

    public static void openBuyShop(Player player) {
        Gui gui = new Gui(4, pl.ItemBuyName);
        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        gui.getFiller().fillBottom(ItemBuilder.from(Material.valueOf(pl.BottomItem)).setName(" ").asGuiItem());
        gui.setItem(4,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            selectMenu(player);
        }));
        gui.setItem(4,5 , ItemBuilder.from(Material.valueOf(pl.CurrentMoneyItem)).setName(pl.CurrentMoney).setLore("§e"+Main.econ.format(Main.econ.getBalance(player))).asGuiItem());

        gui.setItem(2, 5 , ItemBuilder.from(Material.valueOf(material)).setAmount(100).setLore("§7"+pl.econ.format(price)).asGuiItem());

        gui.setItem(2, 3, ItemBuilder.from(Material.valueOf(pl.AcceptItem)).setName(pl.AcceptName).asGuiItem(event -> {

            if (pl.econ.getBalance(player) >= price) {
                if (!(player.getInventory().firstEmpty() == -1)){
                    player.getInventory().addItem(new ItemStack(Material.valueOf(material), 100));
                    pl.econ.withdrawPlayer(player, price);
                    player.sendMessage(pl.ItemBuy.replaceAll("%amount%", amount+"").replaceAll("%item%", material.toLowerCase()).replaceAll("%price%", pl.econ.format(price)));
                } else {
                    player.closeInventory();
                    player.sendMessage(pl.NoSpace);
                }
            } else {
                player.closeInventory();
                player.sendMessage(pl.NoMoney);
            }
        }));
        gui.setItem(2, 7, ItemBuilder.from(Material.valueOf(pl.DenyItem)).setName(pl.DenyName).asGuiItem(event -> {
            player.closeInventory();
        }));




        gui.open(player);
    }





}
