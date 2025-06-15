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

package com.andrewreedhall.ancientscrolls.scroll;

import com.andrewreedhall.ancientscrolls.scroll.player.PlayerStorage;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollPlayerListener implements Listener {
    public ScrollPlayerListener() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        EquipmentSlot hand = event.getHand();
        if (hand == null || !hand.equals(EquipmentSlot.HAND)) {
            return;
        }
        ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }
        Scroll scroll = ScrollManager.getRegisteredScroll(itemStack);
        if (scroll == null) {
            return;
        }
        Player player = event.getPlayer();
        boolean inserted = false;
        if (!scroll.doesPlayerHaveScroll(player)) {
            PlayerStorage.Instance playerStorage = PlayerStorage.access(player);
            int maxPlayerStoredScrolls = plugin().getCachedConfig().scroll_playerStorage_maxStoredScrolls;
            for (int i = 0; i < maxPlayerStoredScrolls; ++i) {
                if (playerStorage.scrolls[i] == null) {
                    playerStorage.scrolls[i] = scroll;
                    inserted = true;
                    break;
                }
            }
        }
        if (!inserted) {
            player.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
            return;
        }
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        itemStack.setAmount(itemStack.getAmount() - 1);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Boolean keepInventory = player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
        if (keepInventory != null && keepInventory) {
            return;
        }
        PlayerStorage.Instance playerStorage = PlayerStorage.access(player);
        int maxPlayerStoredScrolls = plugin().getCachedConfig().scroll_playerStorage_maxStoredScrolls;
        List<ItemStack> drops = event.getDrops();
        for (int i = 0; i < maxPlayerStoredScrolls; ++i) {
            Scroll scroll = playerStorage.scrolls[i];
            if (scroll == null) {
                continue;
            }
            playerStorage.scrolls[i] = null;
            drops.add(scroll.createItemStack());
        }
        PlayerStorage.save(player, playerStorage);
    }
}
