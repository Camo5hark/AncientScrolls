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

import com.andrewreedhall.ancientscrolls.structure.AncientScrollsStructure;
import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.EntityTransformer;

import java.util.Set;

public abstract class StructureNative extends AncientScrollsStructure {
    public StructureNative(final String id, final Set<BlockTransformer> blockTransformers, final Set<EntityTransformer> entityTransformers) {
        super(fromAncientScrollsNamespace(id), blockTransformers, entityTransformers);
    }

    protected static GenerationInfo createGenerationInfoDeepOceanSurface(
            final World world,
            final int blockX,
            final int blockZ,
            final int offsetBlockY,
            final double prob
    ) {
        if (!world.getEnvironment().equals(World.Environment.NORMAL)) {
            return null;
        }
        final Block block = world.getHighestBlockAt(blockX, blockZ);
        if (!block.getType().equals(Material.WATER) || !CommonSets.DEEP_OCEAN_BIOMES.contains(block.getBiome())) {
            return null;
        }
        return new GenerationInfo(prob, block.getY() + offsetBlockY);
    }
}
