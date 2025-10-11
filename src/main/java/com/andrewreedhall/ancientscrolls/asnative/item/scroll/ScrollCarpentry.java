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

package com.andrewreedhall.ancientscrolls.asnative.item.scroll;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;

import static org.bukkit.Material.*;

public final class ScrollCarpentry extends ItemScrollNative implements Listener {
    private static final Set<Material> BLOCK_TYPES = Set.of(
            OAK_LOG,
            OAK_WOOD,
            STRIPPED_OAK_LOG,
            STRIPPED_OAK_WOOD,
            OAK_PLANKS,
            OAK_STAIRS,
            OAK_SLAB,
            OAK_FENCE,

            SPRUCE_LOG,
            SPRUCE_WOOD,
            STRIPPED_SPRUCE_LOG,
            STRIPPED_SPRUCE_WOOD,
            SPRUCE_PLANKS,
            SPRUCE_STAIRS,
            SPRUCE_SLAB,
            SPRUCE_FENCE,

            BIRCH_LOG,
            BIRCH_WOOD,
            STRIPPED_BIRCH_LOG,
            STRIPPED_BIRCH_WOOD,
            BIRCH_PLANKS,
            BIRCH_STAIRS,
            BIRCH_SLAB,
            BIRCH_FENCE,

            JUNGLE_LOG,
            JUNGLE_WOOD,
            STRIPPED_JUNGLE_LOG,
            STRIPPED_JUNGLE_WOOD,
            JUNGLE_PLANKS,
            JUNGLE_STAIRS,
            JUNGLE_SLAB,
            JUNGLE_FENCE,

            ACACIA_LOG,
            ACACIA_WOOD,
            STRIPPED_ACACIA_LOG,
            STRIPPED_ACACIA_WOOD,
            ACACIA_PLANKS,
            ACACIA_STAIRS,
            ACACIA_SLAB,
            ACACIA_FENCE,

            DARK_OAK_LOG,
            DARK_OAK_WOOD,
            STRIPPED_DARK_OAK_LOG,
            STRIPPED_DARK_OAK_WOOD,
            DARK_OAK_PLANKS,
            DARK_OAK_STAIRS,
            DARK_OAK_SLAB,
            DARK_OAK_FENCE,

            MANGROVE_LOG,
            MANGROVE_WOOD,
            STRIPPED_MANGROVE_LOG,
            STRIPPED_MANGROVE_WOOD,
            MANGROVE_PLANKS,
            MANGROVE_STAIRS,
            MANGROVE_SLAB,
            MANGROVE_FENCE,

            CHERRY_LOG,
            CHERRY_WOOD,
            STRIPPED_CHERRY_LOG,
            STRIPPED_CHERRY_WOOD,
            CHERRY_PLANKS,
            CHERRY_STAIRS,
            CHERRY_SLAB,
            CHERRY_FENCE,

            PALE_OAK_LOG,
            PALE_OAK_WOOD,
            STRIPPED_PALE_OAK_LOG,
            STRIPPED_PALE_OAK_WOOD,
            PALE_OAK_PLANKS,
            PALE_OAK_STAIRS,
            PALE_OAK_SLAB,
            PALE_OAK_FENCE,

            BAMBOO_BLOCK,
            STRIPPED_BAMBOO_BLOCK,
            BAMBOO_PLANKS,
            BAMBOO_MOSAIC,
            BAMBOO_STAIRS,
            BAMBOO_MOSAIC_STAIRS,
            BAMBOO_SLAB,
            BAMBOO_MOSAIC_SLAB,
            BAMBOO_FENCE
    );

    public ScrollCarpentry() {
        super("carpentry", "Carpentry", new String[] {
                "15% chance to not consume wood-related overworld blocks when placed"
        });
        this.putMCLootTableGenProb("entities/creaking", 0.25);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.5);
        this.putMCLootTableGenProb("chests/village/village_savanna_house", 0.333);
        this.putMCLootTableGenProb("chests/village/village_taiga_house", 0.333);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        this.reduceConsumedPlacedBlocks(event, BLOCK_TYPES, 0.15);
    }
}
