package de.niklas1623.servershop.utils;

import java.util.ArrayList;
import java.util.List;

import de.niklas1623.servershop.Main;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class MenuManager {
  public static ConfigurationSection yml = ConfigManager.cfg.getConfigurationSection("Settings.menu-items");
  public static int ClickedCategory;
  public static String Category_Name;

    public static void loadShopMenu() {
        yml.getKeys(false).forEach(key -> {
            String name = yml.getString(key + ".Name") != null ? ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(yml.getString(key + ".Name"))) : " ";
            String description = yml.getString(key + ".Desc") != null ? ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(yml.getString(key + ".Desc"))) : "";
            int slot = yml.getInt(key +".Slot") != 0 ? yml.getInt(key +".Slot") : 0;
            String permission = yml.getString(key+ ".Permission");
            if (yml.getBoolean(key + ".Enabled")) {
                String itemID = yml.getString(key + ".Item");

                List<String> lore = new ArrayList<>();

                if(description.length() > 0){
                    lore.add(description);
                }
                assert itemID != null;
                GuiItem menuItem = ItemBuilder.from(Material.valueOf(itemID.toUpperCase())).setName(name).setLore(lore).asGuiItem(event -> {
                        Player player = (Player) event.getWhoClicked();
                        ClickedCategory = Integer.parseInt(key);
                        Category_Name = name;
                        InventoryManager.Category = ClickedCategory;
                    assert permission != null;
                    if (player.hasPermission(permission) || player.hasPermission("servershop.*") || player.hasPermission("servershop.menu.*")){
                        InventoryManager.selectBuyItem(player);
                        } else {
                            player.closeInventory();
                            player.sendMessage(Main.getInstance().NoPerm);
                        }
                    });
                    InventoryManager.menuItem.setItem(slot, menuItem);




            }

        });


    }

    public static HashMap<Integer, String> getCategory(){
        HashMap<Integer, String> list = new HashMap<>();
        yml.getKeys(false).forEach(key -> {
            String name = yml.getString(key + ".Name");
            assert name != null;
            list.put(Integer.valueOf(key), ChatColor.translateAlternateColorCodes('&', name));
        });
        return list;
    }

}
