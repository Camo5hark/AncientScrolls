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
