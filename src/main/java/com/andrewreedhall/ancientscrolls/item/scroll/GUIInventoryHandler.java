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

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class GUIInventoryHandler implements Listener {
    private final List<Inventory> guiPageInventories = new ArrayList<>();

    public GUIInventoryHandler() {}

    public void createPageInventories() {
        Lists.partition(ItemScroll.createListOfAllRegistered(), 45).forEach((final List<ItemScroll> scrolls) -> {
            final Inventory guiPageInventory = plugin().getServer().createInventory(null, 54, "Ancient Scrolls");
            guiPageInventory.addItem(
                    scrolls
                            .stream()
                            .map((final ItemScroll scroll) -> scroll.createItemStack(1)) // TODO maybe add generation info to scroll item
                            .toArray(ItemStack[]::new)
            );
            // TODO add control items
            this.guiPageInventories.add(guiPageInventory);
        });
    }

    public void open(final Player player) {
        player.openInventory(this.guiPageInventories.getFirst());
    }
}
