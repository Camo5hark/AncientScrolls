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

package com.andrewreedhall.ancientscrolls.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.world.LootGenerateEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ItemListener implements Listener {
    public ItemListener() {}

    @EventHandler
    public void onLootGenerate(final LootGenerateEvent event) {
        if (!plugin().getDefaultCachedConfig().item_generation_enabled) {
            return;
        }
        plugin().getItemRegistry().getAll().forEach((final AncientScrollsItem item) -> item.generateByLootTable(event));
    }

    @EventHandler
    public void onBlockDispenseLoot(final BlockDispenseLootEvent event) {
        if (!plugin().getDefaultCachedConfig().item_generation_enabled) {
            return;
        }
        plugin().getItemRegistry().getAll().forEach((final AncientScrollsItem item) -> item.generateByVault(event));
    }
}
