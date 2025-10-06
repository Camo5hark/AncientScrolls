package com.andrewreedhall.ancientscrolls.asnative.structure;

import org.bukkit.World;

public final class StructureFishermanRaft extends StructureNative {
    public StructureFishermanRaft() {
        super("fisherman_raft");
    }

    @Override
    protected GenerationInfo createGenerationInfo(final World world, final int blockX, final int blockZ) {
        return createGenerationInfoDeepOceanSurface(world, blockX, blockZ, 0, 0.001);
    }
}
