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

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class GUIInventoryHandler implements Listener {
    private static final int PREVIOUS_PAGE_BUTTON_SLOT = 45;
    private static final int NEXT_PAGE_BUTTON_SLOT = 53;
    private static final String PMK_GUI_PAGE_INVENTORY_INDEX = "gui_page_inventory_index";

    private final List<Inventory> guiPageInventories = new ArrayList<>();

    public GUIInventoryHandler() {}

    public void createPageInventories() {
        Lists.partition(ItemScroll.createListOfAllRegistered(), 45).forEach((final List<ItemScroll> scrolls) -> {
            final Inventory guiPageInventory = plugin().getServer().createInventory(null, 54, Component.text("Ancient Scrolls"));
            guiPageInventory.addItem(
                    scrolls
                            .stream()
                            .map((final ItemScroll scroll) -> scroll.createItemStackWithGenerationInfo(1))
                            .toArray(ItemStack[]::new)
            );
            putNavigationButtonItemStack(guiPageInventory, PREVIOUS_PAGE_BUTTON_SLOT, Color.RED, "Previous Page", NamedTextColor.RED);
            putNavigationButtonItemStack(guiPageInventory, NEXT_PAGE_BUTTON_SLOT, Color.GREEN, "Next Page", NamedTextColor.GREEN);
            this.guiPageInventories.add(guiPageInventory);
        });
    }

    public void open(final Player player, final int pageIndex) {
        Inventory pageInventory;
        try {
            pageInventory = this.guiPageInventories.get(pageIndex);
        } catch (final IndexOutOfBoundsException e) {
            return;
        }
        player.openInventory(pageInventory);
        player.setMetadata(PMK_GUI_PAGE_INVENTORY_INDEX, new FixedMetadataValue(plugin(), pageIndex));
    }

    private void navigate(final Player player, final int pageIndexIncrement) {
        final List<MetadataValue> playerGUIPageInventoryIndex = player.getMetadata(PMK_GUI_PAGE_INVENTORY_INDEX);
        if (playerGUIPageInventoryIndex.isEmpty()) {
            return;
        }
        this.open(player, playerGUIPageInventoryIndex.getFirst().asInt() + pageIndexIncrement);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !this.guiPageInventories.contains(clickedInventory)) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player clickingPlayer)) {
            return;
        }
        switch (event.getSlot()) {
            case PREVIOUS_PAGE_BUTTON_SLOT -> this.navigate(clickingPlayer, -1);
            case NEXT_PAGE_BUTTON_SLOT -> this.navigate(clickingPlayer, 1);
            default -> {}
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (!this.guiPageInventories.contains(event.getInventory())) {
            return;
        }
        event.getPlayer().removeMetadata(PMK_GUI_PAGE_INVENTORY_INDEX, plugin());
    }

    private static void putNavigationButtonItemStack(
            final Inventory guiPageInventory,
            final int slot,
            final Color arrowColor,
            final String displayName,
            final NamedTextColor displayNameColor
    ) {
        final ItemStack navigationButtonItemStack = new ItemStack(Material.TIPPED_ARROW);
        final PotionMeta navigationButtonItemMeta = (PotionMeta) BukkitUtil.getItemMeta(navigationButtonItemStack);
        if (navigationButtonItemMeta == null) {
            plugin().getLogger().warning("ItemMeta is null for GUI navigation button ItemStack");
            return;
        }
        navigationButtonItemMeta.customName(Component.text(displayName, displayNameColor));
        navigationButtonItemMeta.setColor(arrowColor);
        navigationButtonItemStack.setItemMeta(navigationButtonItemMeta);
        BukkitUtil.hidePotionContentsTooltipDisplay(navigationButtonItemStack);
        guiPageInventory.setItem(slot, navigationButtonItemStack);
    }
}
