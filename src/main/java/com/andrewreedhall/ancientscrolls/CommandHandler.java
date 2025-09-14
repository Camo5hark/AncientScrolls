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

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls;

import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.item.scroll.ItemScroll;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import com.andrewreedhall.ancientscrolls.util.PlayerDataHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;
import static org.bukkit.ChatColor.*;

public final class CommandHandler {
    public static boolean handle(final CommandSender sender, final String name, final String[] args) {
        switch (name) {
            case "asreload" -> {
                plugin().reload();
                sender.sendMessage("Reloaded Ancient Scrolls - check console for details");
                return true;
            }
            case "asgive" -> {
                if (args.length < 3) {
                    break;
                }
                // resolve args
                final String argPlayerName = args[0];
                final Player player = plugin().getServer().getPlayer(argPlayerName);
                if (player == null) {
                    sender.sendMessage(RED + "Player not found: " + argPlayerName);
                    return true;
                }
                final NamespacedKey argItemKey = NamespacedKey.fromString(args[1]);
                if (argItemKey == null) {
                    break;
                }
                final AncientScrollsItem item = plugin().getItemRegistry().get(argItemKey);
                if (item == null) {
                    sender.sendMessage(RED + "Item not found: " + argItemKey);
                    return true;
                }
                int argItemAmount;
                try {
                    argItemAmount = Integer.parseInt(args[2]);
                } catch (final NumberFormatException e) {
                    break;
                }
                // execute
                BukkitUtil.addItem(player.getInventory(), item.createItemStack(argItemAmount));
                sender.sendMessage(GREEN + "Gave " + player.getName() + " x" + argItemAmount + " " + argItemKey);
                return true;
            }
            case "clearequippedscrolls" -> {
                if (args.length < 1) {
                    break;
                }
                // resolve args
                final String argPlayerName = args[0];
                final Player player = plugin().getServer().getPlayer(argPlayerName);
                if (player == null) {
                    sender.sendMessage(RED + "Player not found: " + argPlayerName);
                    return true;
                }
                // execute
                PlayerDataHandler.clearEquippedScrolls(player);
                sender.sendMessage(GREEN + "Cleared " + player.getName() + "'s equipped scrolls");
                return true;
            }
            case "ancientknowledge" -> {
                if (!(sender instanceof Player playerSender)) {
                    sender.sendMessage(RED + "This command can only be executed by players");
                    return true;
                }
                Player player = playerSender;
                if (playerSender.hasPermission("ancientscrolls.ancientknowledge.others") && args.length > 0) {
                    final String argPlayerName = args[0];
                    player = plugin().getServer().getPlayer(argPlayerName);
                    if (player == null) {
                        sender.sendMessage(RED + "Player not found: " + argPlayerName);
                        return true;
                    }
                }
                plugin().getEquippedScrollsInventoryHandler().open(playerSender, player);
                return true;
            }
            case "addequippedscroll" -> {
                if (args.length < 2) {
                    break;
                }
                // resolve args
                final String argPlayerName = args[0];
                final Player player = plugin().getServer().getPlayer(argPlayerName);
                if (player == null) {
                    sender.sendMessage(RED + "Player not found: " + argPlayerName);
                    return true;
                }
                final NamespacedKey argScrollItemKey = NamespacedKey.fromString(args[1]);
                if (argScrollItemKey == null || !(plugin().getItemRegistry().get(argScrollItemKey) instanceof final ItemScroll scroll)) {
                    break;
                }
                // execute
                if (PlayerDataHandler.insertEquippedScroll(player, scroll)) {
                    sender.sendMessage(GREEN + "Added " + scroll.key + " to " + player.getName() + "'s equipped scrolls");
                } else {
                    sender.sendMessage(RED + "Could not add equipped scroll to " + player.getName() + " because they are either already equipping the scroll or at the equipped scrolls limit");
                }
                return true;
            }
            case "asgui" -> {
                if (!(sender instanceof Player playerSender)) {
                    sender.sendMessage(RED + "This command can only be executed by players");
                    return true;
                }
                plugin().getGUIInventoryHandler().open(playerSender, 0);
                return true;
            }
            default -> {}
        }
        return false;
    }
}
