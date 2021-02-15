package de.niklas1623.servershop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.niklas1623.servershop.Main;

public class MoneyCommands implements CommandExecutor {

    public MoneyCommands(Main main) {
        // TODO Auto-generated constructor stub
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().contentEquals("bits")) {
            Main.getInstance();
            Main.getInstance();
            sender.sendMessage(Main.econ.format(Main.econ.getBalance(player)));
        }

        return true;
    }

}
