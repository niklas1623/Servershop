package de.niklas1623.servershop.utils;

import de.niklas1623.servershop.commands.ShopCommands;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.niklas1623.servershop.Main;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class InventoryManager implements Listener {
    private static Main pl = Main.getInstance();
    public static int Category;

    public static Gui menuItem = new Gui(6,  Main.getInstance().ServershopName);

    public static void openServershop(Player player) {
        Gui gui = new Gui(3, pl.ServershopName);
        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        GuiItem buy = ItemBuilder.from(Material.CHEST).setName(pl.ItemBuyName).setLore(pl.ItemBuyDesc).asGuiItem(event -> {
            if(!event.getClick().isLeftClick()){
            }
            selectMenu(player);
        });
        GuiItem sell = ItemBuilder.from(Material.CHEST).setName(pl.ItemSellName).setLore(pl.ItemSellDesc).asGuiItem(event -> {
            openSellShop(player);
        });

        GuiItem money = ItemBuilder.from(Material.valueOf(pl.CurrentMoneyItem)).setName(pl.CurrentMoney).setLore("§e"+Main.econ.format(Main.econ.getBalance(player))).asGuiItem();
        ArrayList<String> info = new ArrayList<>();
        info.add("§8Made by:§7§o niklas1623");
        info.add("§8Version: §7§o"+pl.getDescription().getVersion());

        GuiItem pluginInfo = ItemBuilder.from(Material.COMMAND_BLOCK).setName("§a§l"+pl.getDescription().getName()).setLore(info).asGuiItem();

        gui.setItem(4, pluginInfo);
        gui.setItem(11, buy);
        gui.setItem(15, sell);
        gui.setItem(22, money);
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
        GuiItem search = ItemBuilder.from(Material.valueOf(pl.SearchItem)).setName(pl.SearchName).setLore(pl.SearchDesc).asGuiItem(event -> {
            Player p = (Player) event.getWhoClicked();
            p.closeInventory();
            TextComponent message = new TextComponent(pl.prefix+ " "+pl.SearchText);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/shop search "));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(pl.SearchDesc)));
            p.spigot().sendMessage(message);
        });
        menuItem.setItem(45,back);
        menuItem.setItem(49,money);
        menuItem.setItem(53, search);
        menuItem.open(player);
    }

    public static void selectBuyItem(Player player){
        PaginatedGui shopItem = new PaginatedGui(4,27, MenuManager.Category_Name);

        shopItem.getFiller().fillBottom(ItemBuilder.from(Material.valueOf(pl.BottomItem)).setName(" ").asGuiItem());
        shopItem.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        shopItem.setItem(4,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            selectMenu(player);
        }));
        List<Integer> itemlist = ShopManager.getItems(MenuManager.ClickedCategory, "b");
        if (!(itemlist.size() == 0)) {
            for (int i = 0; i < itemlist.size(); i++) {
                int IDinShop = itemlist.get(i);
                int amount = ShopManager.getAmount(IDinShop);
                double price = ShopManager.getPrice(IDinShop);
                String material = ShopManager.getMaterial(IDinShop);
                shopItem.addItem(ItemBuilder.from(Material.valueOf(material.toUpperCase())).setAmount(amount).setLore("§7" + Main.econ.format(price)).asGuiItem(event -> {
                    String mat = event.getCurrentItem().getType().name();
                    openBuyShop(player, mat, IDinShop);
                }));


            }
            shopItem.open(player);
            if (shopItem.getNextPageNum() > 1){
                shopItem.setItem(4,3, ItemBuilder.from(Material.valueOf(pl.PreviousPageItem)).setName(pl.PreviousPage).asGuiItem(event -> shopItem.previous()));
                shopItem.setItem(4,7, ItemBuilder.from(Material.valueOf(pl.NextPageItem)).setName(pl.NextPage).asGuiItem(event -> shopItem.next()));
            }
            shopItem.update();

        } else {
            player.closeInventory();
            player.sendMessage(pl.NotAvailable);
        }
        itemlist.clear();
    }

    public static void selectSearchItem(Player player){
        PaginatedGui shopItem = new PaginatedGui(4,27, "§b§o§l"+ShopCommands.input.replaceAll("%", ""));

        shopItem.getFiller().fillBottom(ItemBuilder.from(Material.valueOf(pl.BottomItem)).setName(" ").asGuiItem());
        shopItem.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        shopItem.setItem(4,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            selectMenu(player);
        }));
        List<Integer> list = ShopManager.getSearchItem(ShopCommands.input);
        if (!(list.size() == 0)) {
            for (int i = 0; i < list.size(); i++) {
                int IDinShop = ShopManager.getIDinShop("b",list.get(i));
                    int amount = ShopManager.getAmount(IDinShop);
                    double price = ShopManager.getPrice(IDinShop);
                    String material = ShopManager.getMaterial(IDinShop);
                    if (!(IDinShop == 0)){
                        shopItem.addItem(ItemBuilder.from(Material.valueOf(material)).setAmount(amount).setLore("§7" + Main.econ.format(price)).asGuiItem(event -> {
                            String mat = event.getCurrentItem().getType().name();
                            openBuyShop(player, mat, IDinShop);
                        }));
                    }
            }
            shopItem.open(player);
            if (shopItem.getInventory().firstEmpty() == 0) {
                player.closeInventory();
                player.sendMessage(pl.NotFound.replaceAll("%input%", ShopCommands.input.replaceAll("%", "")));
            }
            if (shopItem.getNextPageNum() > 1){
                shopItem.setItem(4,3, ItemBuilder.from(Material.valueOf(pl.PreviousPageItem)).setName(pl.PreviousPage).asGuiItem(event -> shopItem.previous()));
                shopItem.setItem(4,7, ItemBuilder.from(Material.valueOf(pl.NextPageItem)).setName(pl.NextPage).asGuiItem(event -> shopItem.next()));
            }
            shopItem.update();
        } else {
            player.sendMessage(pl.NotFound.replaceAll("%input%", ShopCommands.input.replaceAll("%", "")));
        }
        list.clear();
    }

    public static void openSellShop(Player player) {
        Gui sellShop = new Gui(6, pl.ItemSellName);

        sellShop.getFiller().fillBorder(ItemBuilder.from(Material.valueOf(pl.BorderItem)).setName(" ").asGuiItem(event -> {
            event.setCancelled(true);
        }));
        sellShop.setItem(6,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            event.setCancelled(true);
            openServershop(player);
        }));
        double price = 0;
        sellShop.setItem(6 , 5, ItemBuilder.from(Material.valueOf(pl.SellItem)).setName(pl.SellName.replaceAll("%price%", Main.econ.format(price)+"")).setLore(pl.SellDesc).asGuiItem(event -> {
            event.setCancelled(true);
        }));




        sellShop.setOpenGuiAction(event -> {
            SellManager.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
                double totalSale = SellManager.calculateTotalValue(sellShop.getInventory());
                sellShop.updateItem(6 , 5, ItemBuilder.from(Material.valueOf(Main.getInstance().SellItem)).setName(Main.getInstance().SellName.replaceAll("%price%", Main.econ.format(totalSale)+"")).setLore(Main.getInstance().SellDesc).asGuiItem(event1 -> {
                    event1.setCancelled(true);
                    SellManager.sellAll((Player) event1.getWhoClicked(), sellShop.getInventory());
                }));
            }, 7, 7);
        });
        sellShop.open(player);
        sellShop.setCloseGuiAction(event -> {

        });
    }

    public static void openBuyShop(Player player, String material, int IDinShop) {
        Gui gui = new Gui(4, pl.ItemBuyName);
        final int[] amount = {ShopManager.getAmount(IDinShop)};
        final double[] price = {ShopManager.getPrice(IDinShop)};
        double price_per_one = price[0] / amount[0];

        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        gui.getFiller().fillBottom(ItemBuilder.from(Material.valueOf(pl.BottomItem)).setName(" ").asGuiItem());
        gui.setItem(4,1, ItemBuilder.from(Material.valueOf(pl.OneMenuBackItem)).setName(pl.OneMenuBack).asGuiItem(event -> {
            selectMenu(player);
        }));
        gui.setItem(4,5 , ItemBuilder.from(Material.valueOf(pl.CurrentMoneyItem)).setName(pl.CurrentMoney).setLore("§e"+Main.econ.format(Main.econ.getBalance(player))).asGuiItem());

        gui.setItem(1, 5 , ItemBuilder.from(Material.valueOf(material)).setAmount(amount[0]).setLore("§7"+pl.econ.format(price[0])).asGuiItem());

        gui.setItem(2, 3, ItemBuilder.from(Material.valueOf(pl.AcceptItem)).setName(pl.AcceptName).asGuiItem(event -> {

            if (Main.econ.getBalance(player) >= price[0]) {
                if (!(player.getInventory().firstEmpty() == -1)){
                    player.getInventory().addItem(new ItemStack(Material.valueOf(material), amount[0]));

                    if (Main.econ.hasAccount(pl.AccountName)){
                        player.sendMessage("JA ES GEHT!"+getUUID(pl.AccountName));

                    } else {
                        player.sendMessage("NEIN ES GEHT NICHT");
                    }
                    if (pl.ServerAccount){
                        if (pl.AccountName != null){
                            Main.econ.withdrawPlayer(player, price[0]);
                            addMoneyToServer(price[0]);
                            player.sendMessage(offlinePlayer().getName());
                        } else {
                            player.sendMessage(pl.NoServerShopAccount);
                            Main.econ.withdrawPlayer(player, price[0]);
                        }
                    } else {
                        Main.econ.withdrawPlayer(player, price[0]);
                    }
                    player.sendMessage(pl.ItemBuy.replaceAll("%amount%", amount[0] +"").replaceAll("%item%", material.toLowerCase()).replaceAll("%price%", pl.econ.format(price[0])));
                    gui.updateItem(4,5 , ItemBuilder.from(Material.valueOf(pl.CurrentMoneyItem)).setName(pl.CurrentMoney).setLore("§e"+Main.econ.format(Main.econ.getBalance(player))).asGuiItem());
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
            selectMenu(player);
        }));
        if (pl.MinusItem.equalsIgnoreCase(Material.PLAYER_HEAD.name())) {
            gui.setItem(3, 6, ItemBuilder.from(Material.valueOf(pl.MinusItem)).setSkullTexture(pl.MinusHeadID).setName(pl.MinusName).setLore(pl.MinusDesc).asGuiItem(event -> {
                if (event.isLeftClick()) {
                    amount[0] -= 1;
                } else if (event.isRightClick()) {
                    amount[0] -= 10;
                } else {
                    event.setCancelled(true);
                }
                if (amount[0] < 1) {
                    amount[0] = 1;
                }
                price[0] = price_per_one * amount[0];
                gui.updateItem(1, 5, ItemBuilder.from(Material.valueOf(material)).setAmount(amount[0]).setLore("§7" + pl.econ.format(price[0])).asGuiItem());
            }));
        } else {
            gui.setItem(3, 6, ItemBuilder.from(Material.valueOf(pl.MinusItem)).setName(pl.MinusName).setLore("").asGuiItem(event -> {
                if (event.isLeftClick()) {
                    amount[0] -= 1;
                } else if (event.isRightClick()) {
                    amount[0] -= 10;
                } else {
                    event.setCancelled(true);
                }
                if (amount[0] < 1) {
                    amount[0] = 1;
                }
                price[0] = price_per_one * amount[0];
                gui.updateItem(1, 5 , ItemBuilder.from(Material.valueOf(material)).setAmount(amount[0]).setLore("§7"+pl.econ.format(price[0])).asGuiItem());
            }));
        }

        if (pl.PlusItem.equalsIgnoreCase(Material.PLAYER_HEAD.name())) {
            gui.setItem(3, 4, ItemBuilder.from(Material.valueOf(pl.PlusItem)).setSkullTexture(pl.PlusHeadID).setName(pl.PlusName).setLore(pl.PlusDesc).asGuiItem(event -> {
                if (event.isLeftClick()) {
                    amount[0] += 1;
                } else if (event.isRightClick()) {
                    amount[0] += 10;
                } else {
                    event.setCancelled(true);
                }
                if (amount[0] > 64) {
                    amount[0] = 64;
                }
                price[0] = price_per_one * amount[0];
                gui.updateItem(1, 5 , ItemBuilder.from(Material.valueOf(material)).setAmount(amount[0]).setLore("§7"+pl.econ.format(price[0])).asGuiItem());
            }));
        } else {
            gui.setItem(3, 4, ItemBuilder.from(Material.valueOf(pl.PlusItem)).setName(pl.PlusName).setLore("").asGuiItem(event -> {
                if (event.isLeftClick()) {
                    amount[0] += 1;
                } else if (event.isRightClick()) {
                    amount[0] += 10;
                } else {
                    event.setCancelled(true);
                }
                if (amount[0] > 64) {
                    amount[0] = 64;
                }
                price[0] = price_per_one * amount[0];
                gui.updateItem(1, 5 , ItemBuilder.from(Material.valueOf(material)).setAmount(amount[0]).setLore("§7"+Main.econ.format(price[0])).asGuiItem());
            }));
        }
        gui.open(player);
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
    
    public static void addMoneyToServer(double price){
        EconomyResponse economyResponse = Main.econ.depositPlayer(offlinePlayer(), price);
        if (economyResponse.transactionSuccess()){
            Bukkit.getLogger().log(Level.INFO, pl.getName() + " Added "+Main.econ.format(price)+ " to the Server Account");
        }
    }

    public static OfflinePlayer offlinePlayer(){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getUUID(pl.AccountName));
        return offlinePlayer;
    }

    public static UUID getUUID(String playername) {
        return Bukkit.getOfflinePlayer(playername).getUniqueId();
    }



}
