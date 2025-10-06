package com.andrewreedhall.ancientscrolls.asnative.structure;

import org.bukkit.World;

public final class StructureFloatingIsland extends StructureNative {
    public StructureFloatingIsland() {
        super("floating_island");
    }

    @Override
    protected GenerationInfo createGenerationInfo(final World world, final int blockX, final int blockZ) {
        if (!world.getEnvironment().equals(World.Environment.NORMAL) ||
                ((long) blockX * (long) blockX) + ((long) blockZ * (long) blockZ) < 400000000L
        ) {
            return null;
        }
        return new GenerationInfo(0.0001, Math.max(world.getMinHeight(), world.getMaxHeight() - 30));
    }
}
