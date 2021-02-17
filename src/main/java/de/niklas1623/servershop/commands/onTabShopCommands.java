package de.niklas1623.servershop.commands;

import de.niklas1623.servershop.utils.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class onTabShopCommands implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

        HashMap<Integer, String> category = MenuManager.getCategory();

        List<String> list = new ArrayList<>();
        list.add("add");
        list.add("remove");
        list.add("edit");
        list.add("reload");
        list.add("getid");
        list.add("help");
        list.add("search");
        List<String> list2 = new ArrayList<>();
        list2.add("b");
        list2.add("s");
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();
        list4.add("price");
        list4.add("amount");
        list4.add("category");

        if (cmd.getName().equalsIgnoreCase("shop")){
            if (args.length == 1){
                return list;
            } else if (args[0].equalsIgnoreCase("add")){
                if (args.length == 2){
                    return list2;
                } else if (args.length == 3) {
                    for (Integer key : category.keySet()) {
                        list3.add(key.toString());
                    }
                    return list3;
                }
            } else if (args[0].equalsIgnoreCase("edit")){
                if (args.length == 2) {
                    return list2;
                } else if (args.length == 3){
                    return list4;
                }
            }
        }
        return null;
    }
}
