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

package com.andrewreedhall.ancientscrolls.asnative.structure;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class StructurePillagerShip extends StructureNative {
    public StructurePillagerShip() {
        super("pillager_ship");
    }

    @Override
    protected GenerationInfo createGenerationInfo(final World world, final int blockX, final int blockZ) {
        if (!world.getEnvironment().equals(World.Environment.NORMAL)) {
            return null;
        }
        final Block block = world.getHighestBlockAt(blockX, blockZ);
        if (!block.getType().equals(Material.WATER) || !CommonSets.DEEP_OCEAN_BIOMES.contains(block.getBiome())) {
            return null;
        }
        return new GenerationInfo(0.0025, block.getY() - 1);
    }
}
