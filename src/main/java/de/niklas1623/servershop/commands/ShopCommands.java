package de.niklas1623.servershop.commands;

import de.niklas1623.servershop.Main;
import de.niklas1623.servershop.utils.ConfigManager;
import de.niklas1623.servershop.utils.InventoryManager;
import de.niklas1623.servershop.utils.MenuManager;
import de.niklas1623.servershop.utils.ShopManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;

public class ShopCommands implements CommandExecutor {

    // shop add ShopID Category Price
    // shop getID
    // shop edit ShopID Price/Amount
    // shop remove ShopID

    Main plugin = Main.getInstance();
    public int ItemID;
    public int amount;
    public double price;
    public String ShopType;
    public int category;
    public String mat;
    public static String input;


    public ShopCommands(Main main) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
         int ItemID;
         int amount;
         double price;
         String ShopType;
         int category;
         String mat;
         String input;
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.OnlyPlayer);
            return true;
        }
        Player p = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("shop")) {
            PlayerInventory inv = p.getInventory();
            Material material = inv.getItemInMainHand().getType();
            HashMap<Integer, String> list = MenuManager.getCategory();
                /**
                 *  Add Command
                 *  /shop add <ShopType> <Category> <Price>
                 */
                if (sender.hasPermission("servershop.use") || sender.hasPermission("servershop.*")) {
                    if (args.length == 0) {
                        InventoryManager.openServershop((Player) sender);
                    } else if (args.length > 0) {
                        if(args[0].equalsIgnoreCase("add")) {
                            if (sender.hasPermission("servershop.add") || sender.hasPermission("servershop.*")) {
                                if (args.length == 4) {
                                    if (args[1].equalsIgnoreCase("s") || args[1].equalsIgnoreCase("b")) {
                                        if (Integer.valueOf(args[2]) <= list.size() && Integer.valueOf(args[2]) > 0) {
                                            if (Double.parseDouble(args[3]) > 0) {
                                                ShopType = args[1];
                                                category = Integer.valueOf(args[2]);
                                                price = Double.parseDouble(args[3]);
                                                mat = material.name();
                                                amount = inv.getItemInMainHand().getAmount();
                                                if (!(material == Material.AIR)) {
                                                    if (!(ShopManager.getItemID(mat) == 0)) {
                                                        ItemID = ShopManager.getItemID(mat);
                                                        if (!(ShopManager.checkItemInShop(ItemID, ShopType) == 1)) {
                                                            ShopManager.addItemToShop(ItemID, amount, price, ShopType, category);
                                                            sender.sendMessage(plugin.AddItem.replaceAll("%amount%", amount + "").replaceAll("%price%", plugin.econ.format(price) + "").replaceAll("%item%", mat.toLowerCase()));
                                                        } else sender.sendMessage(plugin.AlreadyInShop);
                                                        return true;
                                                    } else {
                                                        ShopManager.insertItem(mat);
                                                        ItemID = ShopManager.getItemID(mat);
                                                        if (!(ShopManager.checkItemInShop(ItemID, ShopType) == 1)) {
                                                            ShopManager.addItemToShop(ItemID, amount, price, ShopType, category);
                                                            sender.sendMessage(plugin.AddItem.replaceAll("%amount%", amount + "").replaceAll("%price%", plugin.econ.format(price) + "").replaceAll("%item%", mat.toLowerCase()));
                                                        } else sender.sendMessage(plugin.AlreadyInShop);
                                                        return true;
                                                    }
                                                } else sender.sendMessage(plugin.NoAIR);
                                                return true;
                                            } else sender.sendMessage(plugin.PriceNull);
                                            return true;
                                        } else {
                                            sender.sendMessage(plugin.UseCategory);
                                            for (Integer key : list.keySet()) {
                                                sender.sendMessage("§c" + key + " §7" + list.get(key));
                                            }
                                            return true;
                                        }
                                    } else sender.sendMessage(plugin.HowToAdd);
                                return true;
                                } else sender.sendMessage(plugin.HowToAdd);
                                return true;
                            } else sender.sendMessage(plugin.NoPerm);
                            return true;
                            /**
                             * Edit Command for price or amount
                             * /shop edit ShopID Price/Amount/Category <price/amount/category>
                             */
                        } else if (args[0].equalsIgnoreCase("edit")){
                            if (sender.hasPermission("servershop.edit") || sender.hasPermission("servershop.*")) {
                                if (args.length == 4) {
                                    material = inv.getItemInMainHand().getType();
                                if (!(material == Material.AIR)) {
                                    mat = material.name();
                                    ItemID = ShopManager.getItemID(mat);
                                    if (args[1].equalsIgnoreCase("s") || args[1].equalsIgnoreCase("b")) {
                                        ShopType = args[1];
                                        if (!(ShopManager.getItemID(mat) == 0)){
                                            if (ShopManager.checkItemInShop(ItemID, ShopType) == 1) {
                                                if (args[2].equalsIgnoreCase("price")) {
                                                    if (Double.parseDouble(args[3]) > 0) {
                                                        price = Double.parseDouble(args[3]);
                                                        ShopManager.updatePrice(ItemID, ShopType, price);
                                                        sender.sendMessage(plugin.ChangedPrice.replaceAll("%price%", plugin.econ.format(price) + "").replaceAll("%item%", mat.toLowerCase()));
                                                    } else sender.sendMessage(plugin.PriceNull);
                                                    return true;
                                                } else if (args[2].equalsIgnoreCase("amount")) {
                                                    if (Integer.valueOf(args[3]) > 0) {
                                                        ShopType = args[1];
                                                        amount = Integer.valueOf(args[3]);
                                                        ShopManager.updateAmount(ItemID, ShopType, amount);
                                                        sender.sendMessage(plugin.ChangedAmount.replaceAll("%amount%", amount + "").replaceAll("%item%", mat.toLowerCase()));
                                                    } else sender.sendMessage(plugin.WrongAmount);
                                                    return true;
                                                } else if (args[2].equalsIgnoreCase("category")) {
                                                    if (Integer.valueOf(args[3]) <= list.size() && Integer.valueOf(args[3]) > 0) {
                                                        ShopType = args[1];
                                                        category =  Integer.valueOf(args[3]);
                                                        ShopManager.updateCategory(ItemID, ShopType, category);
                                                        sender.sendMessage(plugin.ChangedCategory.replaceAll("%category%", String.valueOf(category)));
                                                    } else {
                                                        sender.sendMessage(plugin.UseCategory);
                                                        for (Integer key : list.keySet()) {
                                                            sender.sendMessage("§c" + key + " §7" + list.get(key));
                                                        }
                                                        return true;
                                                    }
                                                } else sender.sendMessage(plugin.HowToEdit);
                                                return true;
                                            } else sender.sendMessage(plugin.NotInShop);
                                            return true;
                                        } else sender.sendMessage(plugin.NotInShop);
                                        return true;
                                    } else sender.sendMessage(plugin.HowToEdit);
                                    return true;
                                } else sender.sendMessage(plugin.NoAIR);
                                return true;
                                } else sender.sendMessage(plugin.HowToEdit);
                                return true;
                            } else sender.sendMessage(plugin.NoPerm);
                            return true;
                            /**
                             * Remove Command
                             * /shop remove <ShopType>
                             */
                        } else if (args[0].equalsIgnoreCase("remove")){
                            if(sender.hasPermission("servershop.remove") || sender.hasPermission("servershop.*")){
                                if (args.length == 2){
                                    material = inv.getItemInMainHand().getType();
                                    if (args[1].equalsIgnoreCase("s") || args[1].equalsIgnoreCase("b")){
                                        mat = material.name();
                                        ItemID = ShopManager.getItemID(mat);
                                        ShopType = args[1];
                                        if (!(material == Material.AIR)){
                                            if (!(ItemID == 0)){
                                                if (ShopManager.checkItemInShop(ItemID, ShopType) == 1) {
                                                    ShopManager.removeFromShop(ShopType, ItemID);
                                                    sender.sendMessage(plugin.RemoveItem.replaceAll("%item%", mat.toLowerCase()));
                                                } else sender.sendMessage(plugin.NotInShop);
                                                return true;
                                            } else sender.sendMessage(plugin.NotInShop);
                                            return true;
                                        } else sender.sendMessage(plugin.NoAIR);
                                        return true;
                                    } else sender.sendMessage(plugin.HowToRemove);
                                    return true;
                                }else sender.sendMessage(plugin.HowToRemove);
                                return true;
                            } else sender.sendMessage(plugin.NoPerm);
                            return true;
                            /**
                             * GetID
                             * /shop getid
                             */
                        } else if(args[0].equalsIgnoreCase("getid")) {
                            if (sender.hasPermission("servershop.getid") || sender.hasPermission("servershop.*")) {
                                if (args.length == 1) {
                                    material = inv.getItemInMainHand().getType();
                                    mat = material.name();
                                    ItemID = ShopManager.getItemID(mat);
                                    if (!(material == Material.AIR)) {
                                        sender.sendMessage(plugin.IDforItem.replaceAll("%item%", mat.toLowerCase()).replaceAll("%id%", ItemID + ""));
                                    } else sender.sendMessage(plugin.NoAIR);
                                    return true;
                                } else sender.sendMessage(plugin.HowToGetID);
                                return true;
                            } else sender.sendMessage(plugin.NoPerm);
                            return true;

                        } else if (args[0].equalsIgnoreCase("help")) {
                            sender.sendMessage(plugin.HowToAdd);
                            sender.sendMessage(plugin.HowToEdit);
                            sender.sendMessage(plugin.HowToGetID);
                            sender.sendMessage(plugin.HowToRemove);
                        } else if(args[0].equalsIgnoreCase("reload")){
                            if (sender.hasPermission("servershop.reload") || sender.hasPermission("servershop.*")) {
                                plugin.reloadConfig();
                                ConfigManager.readConfig();
                                ConfigManager.readMessages();
                                sender.sendMessage(plugin.Reload);
                            } else sender.sendMessage(plugin.NoPerm);
                            return true;
                        } else if (args[0].equalsIgnoreCase("search") || args[0].equalsIgnoreCase("s")){
                            if (sender.hasPermission("servershop.search") || sender.hasPermission("servershop.*")) {
                                if (args.length == 2){
                                    input = "%"+args[1]+"%";
                                        InventoryManager.selectSearchItem(p);
                                } else {
                                    sender.sendMessage(plugin.HowToSearch);
                                    return true;
                                }
                            } else {
                                sender.sendMessage(plugin.NoPerm);
                                return true;
                            }
                        } else {
                            sender.sendMessage(plugin.Help);
                            return true;
                        }
                    }
                } else sender.sendMessage(plugin.NoPerm);
            return true;

        } return true;
    }
}
