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

package com.andrewreedhall.ancientscrolls.scroll.player;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.ScrollManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Event listener singleton that manages "Ancient Knowledge" inventory behavior
 */
public final class AncientKnowledgeInventoryListener implements Listener {
    /**
     * The maximum amount of slots to store scrolls in "Ancient Knowledge" inventory
     */
    private static final int MAX_SLOTS = 9;
    /**
     * Maps player UID to their currently open "Ancient Knowledge" inventory<br>
     * This guarantees that players can only have a single open inventory at a time
     */
    private static final Map<UUID, Inventory> OPEN_INVENTORIES = new HashMap<>();

    public AncientKnowledgeInventoryListener() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        UUID playerUID = player.getUniqueId();
        Inventory openInventory = OPEN_INVENTORIES.get(playerUID);
        if (openInventory == null) {
            return;
        }
        OPEN_INVENTORIES.remove(playerUID);
        PlayerStorage.Instance playerStorage = PlayerStorage.access(player);
        int maxPlayerStoredScrolls = plugin().getCachedConfig().scroll_playerStorage_maxStoredScrolls;
        for (int i = 0; i < maxPlayerStoredScrolls; ++i) {
            playerStorage.scrolls[i] = ScrollManager.getRegisteredScroll(openInventory.getItem(i));
        }
        PlayerStorage.save(player, playerStorage);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAncientKnowledgeInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !(clickedInventory.getHolder() instanceof Player clicker)) {
            return;
        }
        Inventory ancientKnowledgeInventory = OPEN_INVENTORIES.get(clicker.getUniqueId());
        if (ancientKnowledgeInventory == null) {
            return;
        }
        // discard events that do not involve clicking on an item
        ItemStack clickedItemStack = event.getCurrentItem();
        if (clickedItemStack == null) {
            return;
        }
        // discard click events that do not involve clicking on a scroll
        Scroll clickedRegisteredScroll = ScrollManager.getRegisteredScroll(clickedItemStack);
        if (clickedRegisteredScroll == null) {
            event.setCancelled(true);
            return;
        }
        switch (event.getAction()) {
            default:
                // default deny actions
                event.setCancelled(true);
                break;
            case InventoryAction.PICKUP_ALL:
            case InventoryAction.PICKUP_ONE:
            case InventoryAction.PICKUP_HALF:
            case InventoryAction.PICKUP_SOME:
                break;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof Player player) || OPEN_INVENTORIES.get(player.getUniqueId()) == null) {
            return;
        }
        event.setCancelled(true);
    }

    /**
     *
     * @return a new item stack representing an unavailable item slot
     */
    private static ItemStack createUnavailableSlotItemStack() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            throw new NullPointerException("itemMeta == null");
        }
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Opens the "Ancient Knowledge" inventory for the player and maps the player to that inventory
     * @param player an online player
     */
    public static void open(Player player) {
        PlayerStorage.Instance playerStorage = PlayerStorage.access(player);
        Inventory ancientKnowledgeInventory = plugin().getServer().createInventory(player, MAX_SLOTS, "Ancient Knowledge");
        int maxPlayerStoredScrolls = plugin().getCachedConfig().scroll_playerStorage_maxStoredScrolls;
        for (int i = 0; i < MAX_SLOTS; ++i) {
            ItemStack slotItemStack = null;
            if (i < maxPlayerStoredScrolls) {
                Scroll scroll = playerStorage.scrolls[i];
                if (scroll != null) {
                    slotItemStack = scroll.createItemStack();
                }
            } else {
                slotItemStack = createUnavailableSlotItemStack();
            }
            ancientKnowledgeInventory.setItem(i, slotItemStack);
        }
        player.openInventory(ancientKnowledgeInventory);
        OPEN_INVENTORIES.put(player.getUniqueId(), ancientKnowledgeInventory);
    }
}
