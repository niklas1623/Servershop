package de.niklas1623.servershop.utils;

import de.niklas1623.servershop.database.MySQL;

import java.net.Inet4Address;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {
    public static List<Integer> item = new ArrayList<>();
    public static List<Integer> itemID = new ArrayList<>();

    public static void insertItem(String mat){
        String insertItem = "INSERT INTO items (Material) VALUES (?)";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(insertItem);
            ps.setString(1, mat);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getItemID(String mat){
        String getItemID = "SELECT ItemID FROM items WHERE Material = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(getItemID);
            ps.setString(1, mat);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getInt("ItemID");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getIDinShop(String ShopType, int ItemID){
        String getIDinShop = "SELECT IDinShop FROM shop WHERE ShopType = ? AND ItemID = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(getIDinShop);
            ps.setString(1, ShopType);
            ps.setInt(2, ItemID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getInt("IDinShop");

            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return 0;
    }

    public static List<Integer> getSearchItem(String Input){
        String getItemID = "SELECT ItemID FROM items WHERE Material LIKE ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(getItemID);
            ps.setString(1, Input);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                itemID.add(rs.getInt("ItemID"));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemID;
    }

    public static void addItemToShop(int ItemID, int amount, double price, String ShopType, int category){
        String addItemToShop = "INSERT INTO shop (ItemID, Amount, Price, ShopType, Category) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(addItemToShop);
            ps.setInt(1, ItemID);
            ps.setInt(2, amount);
            ps.setDouble(3, price);
            ps.setString(4, ShopType);
            ps.setInt(5, category);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int checkItemInShop(int ItemID, String ShopType){
        String checkItemInShop = "SELECT COUNT(*) FROM shop WHERE ItemID = ? AND ShopType = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(checkItemInShop);
            ps.setInt(1, ItemID);
            ps.setString(2, ShopType);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return rs.getInt(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getPrice(int IDinShop){
        String getPrice = "SELECT Price FROM shop WHERE IDinShop = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(getPrice);
            ps.setInt(1, IDinShop);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getDouble("Price");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getAmount(int IDinShop){
        String getPrice = "SELECT Amount FROM shop WHERE IDinShop = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(getPrice);
            ps.setInt(1, IDinShop);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getInt("Amount");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMaterial(int IDinShop){
        String getMaterial = "SELECT Material FROM items join shop using(ItemID) WHERE IDinShop = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(getMaterial);
            ps.setInt(1, IDinShop);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString("Material");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePrice(int ItemID, String ShopType, double price){
        String updatePrice = "UPDATE shop SET Price = ? WHERE ItemID = ? AND ShopType = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(updatePrice);
            ps.setDouble(1, price);
            ps.setInt(2, ItemID);
            ps.setString(3, ShopType);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAmount(int ItemID, String ShopType, int amount){
        String updatePrice = "UPDATE shop SET Amount = ? WHERE ItemID = ? AND ShopType = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(updatePrice);
            ps.setDouble(1, amount);
            ps.setInt(2, ItemID);
            ps.setString(3, ShopType);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromShop(String ShopType, int ItemID){
        String removeFromShop = "DELETE FROM shop WHERE ItemID = ? AND ShopType = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(removeFromShop);
            ps.setInt(1, ItemID);
            ps.setString(2, ShopType);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException r) {
            r.printStackTrace();
        }
    }

    public static List<Integer> getItems(int Category, String ShopType){
        String getItems = "SELECT IDinShop FROM shop WHERE Category = ? AND ShopType = ?";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement(getItems);
            ps.setInt(1, Category);
            ps.setString(2, ShopType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                item.add(rs.getInt("IDinShop"));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }


}
