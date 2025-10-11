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

public final class ScrollMasonry extends ItemScrollNative implements Listener {
    private static final Set<Material> BLOCK_TYPES = Set.of(
            STONE,
            STONE_STAIRS,
            STONE_SLAB,

            COBBLESTONE,
            COBBLESTONE_STAIRS,
            COBBLESTONE_SLAB,
            COBBLESTONE_WALL,

            MOSSY_COBBLESTONE,
            MOSSY_COBBLESTONE_STAIRS,
            MOSSY_COBBLESTONE_SLAB,
            MOSSY_COBBLESTONE_WALL,

            SMOOTH_STONE,
            SMOOTH_STONE_SLAB,

            STONE_BRICKS,
            CRACKED_STONE_BRICKS,
            STONE_BRICK_STAIRS,
            STONE_BRICK_SLAB,
            STONE_BRICK_WALL,
            CHISELED_STONE_BRICKS,

            MOSSY_STONE_BRICKS,
            MOSSY_STONE_BRICK_STAIRS,
            MOSSY_STONE_BRICK_SLAB,
            MOSSY_STONE_BRICK_WALL,

            GRANITE,
            GRANITE_STAIRS,
            GRANITE_SLAB,
            GRANITE_WALL,

            POLISHED_GRANITE,
            POLISHED_GRANITE_STAIRS,
            POLISHED_GRANITE_SLAB,

            DIORITE,
            DIORITE_STAIRS,
            DIORITE_SLAB,
            DIORITE_WALL,

            POLISHED_DIORITE,
            POLISHED_DIORITE_STAIRS,
            POLISHED_DIORITE_SLAB,

            ANDESITE,
            ANDESITE_STAIRS,
            ANDESITE_SLAB,
            ANDESITE_WALL,

            POLISHED_ANDESITE,
            POLISHED_ANDESITE_STAIRS,
            POLISHED_ANDESITE_SLAB,

            DEEPSLATE,
            COBBLED_DEEPSLATE,
            COBBLED_DEEPSLATE_STAIRS,
            COBBLED_DEEPSLATE_WALL,

            TUFF,
            TUFF_STAIRS,
            TUFF_SLAB,
            TUFF_WALL,

            CHISELED_TUFF,
            POLISHED_TUFF,
            POLISHED_TUFF_STAIRS,
            POLISHED_TUFF_SLAB,
            POLISHED_TUFF_WALL,

            TUFF_BRICKS,
            TUFF_BRICK_STAIRS,
            TUFF_BRICK_SLAB,
            TUFF_BRICK_WALL,
            CHISELED_TUFF_BRICKS,

            SANDSTONE,
            SANDSTONE_STAIRS,
            SANDSTONE_SLAB,
            SANDSTONE_WALL,

            CHISELED_SANDSTONE,
            SMOOTH_SANDSTONE,
            SMOOTH_SANDSTONE_STAIRS,
            SMOOTH_SANDSTONE_SLAB,
            CUT_SANDSTONE,
            CUT_SANDSTONE_SLAB,

            RED_SANDSTONE,
            RED_SANDSTONE_STAIRS,
            RED_SANDSTONE_SLAB,
            RED_SANDSTONE_WALL,

            CHISELED_RED_SANDSTONE,
            SMOOTH_RED_SANDSTONE,
            SMOOTH_RED_SANDSTONE_STAIRS,
            SMOOTH_RED_SANDSTONE_SLAB,
            CUT_RED_SANDSTONE,
            CUT_RED_SANDSTONE_SLAB
    );

    public ScrollMasonry() {
        super("masonry", "Masonry", new String[] {
                "15% chance to not consume stone-related overworld blocks when placed"
        });
        this.putMCLootTableGenProb("chests/village/village_mason", 0.208);
        this.putMCLootTableGenProb("chests/stronghold_crossing", 0.475);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        this.reduceConsumedPlacedBlocks(event, BLOCK_TYPES, 0.15);
    }
}
