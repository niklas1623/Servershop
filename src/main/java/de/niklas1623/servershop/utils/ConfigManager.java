package de.niklas1623.servershop.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.niklas1623.servershop.Main;
import de.niklas1623.servershop.database.MySQL;

public class ConfigManager {

    private static File customConfigFile;

    public static FileConfiguration cfg;
    public static FileConfiguration MsgConfig;

    public static Main pl = Main.getInstance();


    public static File getConfigFile() {
        return new File("plugins/Servershop", "config.yml");
    }

    public static void createMessagesConfig(){
        customConfigFile = new File(Main.getPlugin(Main.class).getDataFolder(), "messages.yml");
        if(!customConfigFile.exists()){
            customConfigFile.getParentFile().mkdirs();
            Main.getPlugin(Main.class).saveResource("messages.yml", false);
        }
        MsgConfig = new YamlConfiguration();
        try {
            MsgConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfigFileConfiguration() {
        return YamlConfiguration.loadConfiguration(getConfigFile() );
    }

    public static FileConfiguration getMessageFileConfiguration() {
        return MsgConfig;
    }

    public static void readConfig() {
        cfg = getConfigFileConfiguration();
        pl.prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("Prefix") + "§r");
        pl.ServershopName = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Servershop.Name"));
        pl.NextPage = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.Next.Name"));
        pl.PreviousPage = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.Previous.Name"));
        pl.NextPageItem = cfg.getString("Settings.Items.Next.Item");
        pl.PreviousPageItem = cfg.getString("Settings.Items.Previous.Item");
        pl.CurrentMoney = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.CurrentMoney.Name"));
        pl.CurrentMoneyItem = cfg.getString("Settings.Items.CurrentMoney.Item");
        pl.OneMenuBack = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.OneMenuBack.Name"));
        pl.OneMenuBackItem = cfg.getString("Settings.Items.OneMenuBack.Item");
        pl.FillingItem = cfg.getString("Settings.Decoration.FillingItem");
        pl.BorderItem = cfg.getString("Settings.Decoration.BorderItem");
        pl.BottomItem = cfg.getString("Settings.Decoration.BottomItem");
        pl.ItemBuyName = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.ItemBuy.Name"));
        pl.ItemBuyDesc = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.ItemBuy.Desc"));
        pl.ItemSellName = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.ItemSell.Name"));
        pl.ItemSellDesc = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.ItemSell.Desc"));
        pl.AcceptName = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.Accept.Name"));
        pl.DenyName = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.Deny.Name"));
        pl.AcceptItem = cfg.getString("Settings.Items.Accept.Item");
        pl.DenyItem = cfg.getString("Settings.Items.Deny.Item");
        pl.SellDesc = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.Sell.Desc"));
        pl.SellItem = cfg.getString("Settings.Items.Sell.Item");
        pl.SellName = ChatColor.translateAlternateColorCodes('&', cfg.getString("Settings.Items.Sell.Name"));

        MySQL.username = cfg.getString("Database.username");
        MySQL.password = cfg.getString("Database.password");
        MySQL.database = cfg.getString("Database.database");
        MySQL.host = cfg.getString("Database.host");
        MySQL.port = cfg.getString("Database.port");
        MySQL.options = cfg.getString("Database.Options");

    }

    public static void readMessages(){
        MsgConfig = getMessageFileConfiguration();
        pl.NoPerm = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("NoPerm").replaceAll("%prefix%", pl.prefix));
        pl.NoMoney = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("NoMoney").replaceAll("%prefix%", pl.prefix));
        pl.NoSpace = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("NoSpace").replaceAll("%prefix%", pl.prefix));
        pl.NoItem = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("NoItem").replaceAll("%prefix%", pl.prefix));
        pl.ItemBuy = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("ItemBuy").replaceAll("%prefix%", pl.prefix));
        pl.ItemSell = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("ItemSell").replaceAll("%prefix%", pl.prefix));
        pl.AddItem = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("AddItem").replaceAll("%prefix%", pl.prefix));
        pl.RemoveItem = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("RemoveItem").replaceAll("%prefix%", pl.prefix));
        pl.ComingSoon = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("ComingSoon").replaceAll("%prefix%", pl.prefix));
        pl.HowToAdd = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("HowToAdd").replaceAll("%prefix%", pl.prefix));
        pl.HowToRemove = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("HowToRemove").replaceAll("%prefix%", pl.prefix));
        pl.HowToEdit = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("HowToEdit").replaceAll("%prefix%", pl.prefix));
        pl.OnlyPlayer = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("OnlyPlayer").replaceAll("%prefix%", pl.prefix));
        pl.UseCategory = ChatColor.translateAlternateColorCodes('&',MsgConfig.getString("UseCategory").replaceAll("%prefix%", pl.prefix));
        pl.PriceNull = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("PriceNull").replaceAll("%prefix%", pl.prefix));
        pl.NoAIR = ChatColor.translateAlternateColorCodes('&',MsgConfig.getString("NoAIR").replaceAll("%prefix%", pl.prefix));
        pl.AlreadyInShop = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("AlreadyInShop").replaceAll("%prefix%", pl.prefix));
        pl.NotInShop = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("NotInShop").replaceAll("%prefix%", pl.prefix));
        pl.WrongAmount = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("WrongAmount").replaceAll("%prefix%", pl.prefix));
        pl.ChangedAmount = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("ChangedAmount").replaceAll("%prefix%", pl.prefix));
        pl.ChangedPrice = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("ChangedPrice").replaceAll("%prefix%", pl.prefix));
        pl.IDforItem = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("IDforItem").replaceAll("%prefix%", pl.prefix));
        pl.HowToGetID = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("HowToGetID").replaceAll("%prefix%", pl.prefix));
        pl.Help = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("Help").replaceAll("%prefix%", pl.prefix));
        pl.Reload = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("Reload").replaceAll("%prefix%", pl.prefix));
        pl.NotAvailable = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("NotAvailable").replaceAll("%prefix%", pl.prefix));
        pl.HowToSearch = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("HowToSearch").replaceAll("%prefix%", pl.prefix));
        pl.NotFound = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("NotFound").replaceAll("%prefix%", pl.prefix));
        pl.ChangedCategory = ChatColor.translateAlternateColorCodes('&', MsgConfig.getString("ChangedCategory").replaceAll("%prefix%", pl.prefix));
    }

}
