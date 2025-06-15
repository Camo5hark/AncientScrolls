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

package com.andrewreedhall.ancientscrolls.trimbonus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Exists so if admin removes plugin from their server, template/armor lore is not sustained
 */
public class TrimBonusInventoryListener implements Listener {
    public TrimBonusInventoryListener() {}

    /*
     * works for every inventory EXCEPT OF COURSE the player inventory (who tf knows honestly)
     * my amazing workaround is below (onEntityPickupItem and onEntityDropItem)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!plugin().getCachedConfig().trimBonus_enabled) {
            return;
        }
        ItemStack[] inventoryContents = event.getInventory().getContents();
        for (ItemStack inventoryItemStack : inventoryContents) {
            if (inventoryItemStack == null) {
                continue;
            }
            setItemStackLore(inventoryItemStack);
        }
    }

    // seems to work for every inventory
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        ItemStack[] inventoryContents = event.getInventory().getContents();
        for (ItemStack inventoryItemStack : inventoryContents) {
            if (inventoryItemStack == null) {
                continue;
            }
            TrimBonus trimBonus = TrimBonus.TEMPLATE_TYPE_TO_BONUS.get(inventoryItemStack.getType());
            if (trimBonus == null) {
                continue;
            }
            deleteItemStackLore(inventoryItemStack);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!plugin().getCachedConfig().trimBonus_enabled) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        setItemStackLore(event.getItem().getItemStack());
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        ItemStack droppedItemStack = event.getItemDrop().getItemStack();
        TrimBonus trimBonus = TrimBonus.TEMPLATE_TYPE_TO_BONUS.get(droppedItemStack.getType());
        if (trimBonus == null) {
            return;
        }
        deleteItemStackLore(droppedItemStack);
    }

    /**
     * Only sets the item stack lore to a trim bonus lore if it is a template type or armor type with a template, fails silently otherwise
     * @param itemStack
     */
    private static void setItemStackLore(ItemStack itemStack) {
        TrimBonus trimBonus;
        if (itemStack.getItemMeta() instanceof ArmorMeta armorMeta) {
            ArmorTrim armorTrim = armorMeta.getTrim();
            if (armorTrim == null) {
                return;
            }
            trimBonus = TrimBonus.PATTERN_TO_BONUS.get(armorTrim.getPattern());
        } else {
            trimBonus = TrimBonus.TEMPLATE_TYPE_TO_BONUS.get(itemStack.getType());
        }
        if (trimBonus == null) {
            return;
        }
        trimBonus.setItemStackLore(itemStack);
    }

    private static void deleteItemStackLore(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        itemMeta.setLore(null);
        itemStack.setItemMeta(itemMeta);
    }
}
