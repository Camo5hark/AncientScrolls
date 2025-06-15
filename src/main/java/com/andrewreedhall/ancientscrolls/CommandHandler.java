/*

    AncientScrolls SpigotMC plugin
    Copyright (C) 2025  Andrew Hall

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    License file: COPYING
    Email for contact: camo5hark10@gmail.com

 */

package com.andrewreedhall.ancientscrolls;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.ScrollManager;
import com.andrewreedhall.ancientscrolls.scroll.player.AncientKnowledgeInventoryListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler {
    public static boolean handleCommand(CommandSender sender, String name, String[] args) {
        switch (name) {
            default -> {}
            case "asreload" -> {
                plugin().reload();
                return true;
            }
            case "asgive" -> {
                if (args.length < 2) {
                    return false;
                }
                String playerName = args[0];
                Player player = plugin().getServer().getPlayer(playerName);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
                    return true;
                }
                String scrollID = args[1];
                Scroll scroll = ScrollManager.getRegisteredScroll(scrollID);
                if (scroll == null) {
                    sender.sendMessage(ChatColor.RED + "Scroll not found \"" + scrollID + "\"");
                    return true;
                }
                HashMap<Integer, ItemStack> notAddedItems = player.getInventory().addItem(scroll.createItemStack());
                if (!notAddedItems.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Could not give " + playerName + " scroll because their inventory is full");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Gave scroll \"" + scrollID + "\" to " + playerName);
                return true;
            }
            case "ancientknowledge" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can run this command");
                    return true;
                }
                AncientKnowledgeInventoryListener.open(player);
                return true;
            }
        }
        return false;
    }
}
