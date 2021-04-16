package de.niklas1623.servershop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import de.niklas1623.servershop.Main;

public class MySQL {

    public static String username;
    public static String password;
    public static String database;
    public static String host;
    public static String port;
    public static String options;
    public static Connection con;

    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/" + database + options,
                        username, password);
                Bukkit.getConsoleSender().sendMessage(Main.getInstance().prefix + " MySQL Verbindung §aaufgebaut§7!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                Bukkit.getConsoleSender()
                        .sendMessage(Main.getInstance().prefix + " MySQL Verbindung §cfehlgeschlagen§7!");
            }
        }
    }

    public static void close() {
        if (isConnected()) {
            try {
                con.close();
                Bukkit.getConsoleSender().sendMessage(Main.getInstance().prefix + " MySQL Verbindung §cgeschlossen§r!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return con != null;
    }

    public static void createTable() {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS items (Material TEXT NOT NULL , ItemID INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (ItemID))");
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS shop (IDinShop INT NOT NULL AUTO_INCREMENT , ItemID INT NOT NULL , Amount INT NOT NULL , Price INT NOT NULL , ShopType VARCHAR(1) NOT NULL , Category INT NOT NULL , PRIMARY KEY (IDinShop), UNIQUE (ItemID, ShopType))");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
