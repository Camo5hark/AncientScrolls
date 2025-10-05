package com.andrewreedhall.ancientscrolls.asnative.structure;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class StructureFishermanRaft extends StructureNative {
    public StructureFishermanRaft() {
        super("fisherman_raft");
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
        return new GenerationInfo(0.005, block.getY());
    }
}
