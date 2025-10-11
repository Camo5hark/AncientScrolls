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

public final class ScrollNetherSustainability extends ItemScrollNative implements Listener {
    private static final Set<Material> BLOCK_TYPES = Set.of(
            CRIMSON_STEM,
            CRIMSON_HYPHAE,
            STRIPPED_CRIMSON_STEM,
            STRIPPED_CRIMSON_HYPHAE,
            CRIMSON_PLANKS,
            CRIMSON_STAIRS,
            CRIMSON_SLAB,
            CRIMSON_FENCE,

            WARPED_STEM,
            WARPED_HYPHAE,
            STRIPPED_WARPED_STEM,
            STRIPPED_WARPED_HYPHAE,
            WARPED_PLANKS,
            WARPED_STAIRS,
            WARPED_SLAB,
            WARPED_FENCE,

            NETHERRACK,
            NETHER_BRICKS,
            CRACKED_NETHER_BRICKS,
            NETHER_BRICK_STAIRS,
            NETHER_BRICK_SLAB,
            NETHER_BRICK_WALL,
            RED_NETHER_BRICKS,
            RED_NETHER_BRICK_STAIRS,
            RED_NETHER_BRICK_SLAB,
            RED_NETHER_BRICK_WALL,

            BASALT,
            SMOOTH_BASALT,
            POLISHED_BASALT,
            BLACKSTONE,
            BLACKSTONE_STAIRS,
            BLACKSTONE_SLAB,
            BLACKSTONE_WALL,
            CHISELED_POLISHED_BLACKSTONE,
            POLISHED_BLACKSTONE,
            POLISHED_BLACKSTONE_STAIRS,
            POLISHED_BLACKSTONE_SLAB,
            POLISHED_BLACKSTONE_WALL,
            POLISHED_BLACKSTONE_BRICKS,
            CRACKED_POLISHED_BLACKSTONE_BRICKS,
            POLISHED_BLACKSTONE_BRICK_STAIRS,
            POLISHED_BLACKSTONE_BRICK_SLAB,
            POLISHED_BLACKSTONE_BRICK_WALL
    );

    public ScrollNetherSustainability() {
        super("nether_sustainability", "Nether Sustainability", new String[] {
                "15% chance to not consume nether stone and wood blocks when placed"
        });
        this.putMCLootTableGenProb("chests/nether_bridge", 0.179);
        this.putMCLootTableGenProb("chests/bastion_other", 0.244);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        this.reduceConsumedPlacedBlocks(event, BLOCK_TYPES, 0.15);
    }
}
