package de.niklas1623.servershop;

import de.niklas1623.servershop.commands.ShopCommands;
import de.niklas1623.servershop.commands.onTabShopCommands;
import de.niklas1623.servershop.utils.InventoryManager;
import de.niklas1623.servershop.utils.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.niklas1623.servershop.commands.MoneyCommands;
import de.niklas1623.servershop.database.MySQL;
import net.milkbowl.vault.economy.Economy;
import de.niklas1623.servershop.utils.ConfigManager;

public class Main extends JavaPlugin {

    public static Economy econ;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigManager.readConfig();
        ConfigManager.createMessagesConfig();
        ConfigManager.readMessages();
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(prefix+ " Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getConsoleSender().sendMessage(prefix + " Vault wurde gefunden!");
        registerEvents();
        registerCommands();
        MySQL.connect();
        MySQL.createTable();
        Bukkit.getConsoleSender().sendMessage(prefix + " Plugin wurde §ageladen§r!");

    }

    @Override
    public void onDisable() {
        MySQL.close();
        Bukkit.getConsoleSender().sendMessage(prefix + " §cPlugin wurde gestoppt!");
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new InventoryManager(), this);
    }

    private void registerCommands() {
        MoneyCommands mCMD = new MoneyCommands(this);
        getCommand("bits").setExecutor(mCMD);
        ShopCommands sCMD = new ShopCommands(this);
        getCommand("shop").setExecutor(sCMD);
        getCommand("shop").setTabCompleter(new onTabShopCommands());
    }

    public static Main instance;
    public String prefix;
    public String NoMoney;
    public String NoPerm;
    public String NoItem;
    public String NoSpace;
    public String ItemBuy;
    public String ItemSell;
    public String AddItem;
    public String RemoveItem;
    public String ServershopName;
    public String ComingSoon;
    public String HowToAdd;
    public String HowToRemove;
    public String HowToEdit;
    public String OnlyPlayer;
    public String UseCategory;
    public String PriceNull;
    public String NoAIR;
    public String AlreadyInShop;
    public String NotInShop;
    public String WrongAmount;
    public String ChangedPrice;
    public String ChangedAmount;
    public String IDforItem;
    public String HowToGetID;
    public String Help;
    public String Reload;
    public String NextPage;
    public String PreviousPage;
    public String NextPageItem;
    public String PreviousPageItem;
    public String CurrentMoney;
    public String CurrentMoneyItem;
    public String BorderItem;
    public String BottomItem;
    public String FillingItem;
    public String OneMenuBack;
    public String OneMenuBackItem;
    public String ItemBuyName;
    public String ItemBuyDesc;
    public String ItemSellName;
    public String ItemSellDesc;
    public String AcceptName;
    public String DenyName;
    public String AcceptItem;
    public String DenyItem;
    public String NotAvailable;
    public String HowToSearch;
    public String NotFound;
    public String SellItem;
    public String SellName;
    public String SellDesc;
    public String ChangedCategory;


    public static Main getInstance() {
        return instance;
    }


    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    public Economy getEconomy() {
        return econ;
    }

}
