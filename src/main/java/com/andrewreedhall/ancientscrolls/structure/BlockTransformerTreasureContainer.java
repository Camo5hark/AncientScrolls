package com.andrewreedhall.ancientscrolls.structure;

import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record BlockTransformerTreasureContainer(
        Class<? extends Container> containerType,
        Supplier<ItemStack[]> treasureItemStacks
) implements BlockTransformer {
    @Override
    public @NotNull BlockState transform(
            @NotNull final LimitedRegion region,
            final int x,
            final int y,
            final int z,
            @NotNull final BlockState current,
            @NotNull final TransformationState state
    ) {
        if (!this.containerType.isInstance(current)) {
            return current;
        }
        ((Container) current).getInventory().addItem(this.treasureItemStacks.get());
        return current;
    }
}
