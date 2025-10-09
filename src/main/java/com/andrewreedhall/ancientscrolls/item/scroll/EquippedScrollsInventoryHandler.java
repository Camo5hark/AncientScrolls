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

package com.andrewreedhall.ancientscrolls.item.scroll;

import com.andrewreedhall.ancientscrolls.PlayerDataHandler;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Handles the display and synchronization of equipped scrolls inventories per player.
 */
public final class EquippedScrollsInventoryHandler implements Listener, Runnable {
    private static final Component INVENTORY_TITLE = Component.text("Ancient Knowledge");
    private static final Component BARRIER_DISPLAY_NAME = Component.text(" ");

    private final Map<Player, Inventory> equippedScrollInventories = new HashMap<>();

    public EquippedScrollsInventoryHandler() {}

    @Override
    public void run() {
        this.equippedScrollInventories.forEach((final Player player, final Inventory equippedScrollInventory) -> {
            final ItemScroll[] playerEquippedScrolls = PlayerDataHandler.getEquippedScrolls(player);
            for (int i = 0; i < equippedScrollInventory.getSize(); ++i) {
                ItemStack itemStack;
                if (i < playerEquippedScrolls.length) {
                    final ItemScroll playerEquippedScroll = playerEquippedScrolls[i];
                    itemStack = playerEquippedScroll == null ? null : playerEquippedScroll.createItemStack(1);
                } else {
                    itemStack = new ItemStack(Material.BARRIER);
                    final ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
                    itemMeta.displayName(BARRIER_DISPLAY_NAME);
                    itemStack.setItemMeta(itemMeta);
                }
                equippedScrollInventory.setItem(i, itemStack);
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final int maxEquippedScrolls = plugin().getMainConfig().item_scroll_maxEquippedScrolls;
        final int equippedScrollInventorySize = ((maxEquippedScrolls / 9) + (maxEquippedScrolls % 9 == 0 ? 0 : 1)) * 9;
        this.equippedScrollInventories.put(player, plugin().getServer().createInventory(player, equippedScrollInventorySize, INVENTORY_TITLE));
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.equippedScrollInventories.remove(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !(event.getWhoClicked() instanceof Player clickerPlayer)) {
            return;
        }
        final Inventory equippedScrollInventory = event.getView().getTopInventory();
        if (!this.equippedScrollInventories.containsValue(equippedScrollInventory)) {
            return;
        }
        event.setCancelled(true);
        final ItemStack scrollItemStack = event.getCurrentItem();
        if (scrollItemStack == null || !ItemScroll.is(scrollItemStack)) {
            return;
        }
        final Inventory clickerInventory = clickerPlayer.getInventory();
        final int clickedSlot = event.getSlot();
        final Player equippedScrollPlayer = (Player) equippedScrollInventory.getHolder();
        if (clickedInventory.equals(equippedScrollInventory)) {
            equippedScrollInventory.setItem(clickedSlot, null);
            PlayerDataHandler.removeEquippedScroll(equippedScrollPlayer, clickedSlot);
            BukkitUtil.addItem(clickerInventory, scrollItemStack);
        } else if (clickedInventory.equals(clickerInventory) && PlayerDataHandler.insertEquippedScroll(equippedScrollPlayer, (ItemScroll) plugin().getItemRegistry().get(ItemScroll.getKey(scrollItemStack)))) {
            clickerInventory.setItem(clickedSlot, null);
        }
    }

    /**
     * Opens the equipped scrolls inventory of a target player for the given player.
     * @param openingPlayer the player who will view the inventory
     * @param equippedScrollsPlayer the player whose scrolls inventory is being viewed
     */
    public void open(final Player openingPlayer, final Player equippedScrollsPlayer) {
        final Inventory equippedScrollInventory = this.equippedScrollInventories.get(equippedScrollsPlayer);
        if (equippedScrollInventory == null) {
            plugin().getLogger().warning(equippedScrollsPlayer.getName() + " does not have an equipped scrolls inventory");
            return;
        }
        openingPlayer.openInventory(equippedScrollInventory);
        openingPlayer.playSound(openingPlayer, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
    }
}
