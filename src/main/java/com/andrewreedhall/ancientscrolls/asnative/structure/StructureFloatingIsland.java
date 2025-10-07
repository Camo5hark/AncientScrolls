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

import com.andrewreedhall.ancientscrolls.structure.BlockTransformerTreasureContainer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class StructureFloatingIsland extends StructureNative {
    public StructureFloatingIsland() {
        super("floating_island", Set.of(
                new BlockTransformerTreasureContainer(Chest.class, () -> new ItemStack[] {
                        plugin().getItemRegistry().get(fromAncientScrollsNamespace("acrobatics")).createItemStack(1)
                })
        ), null);
    }

    @Override
    protected GenerationInfo createGenerationInfo(final World world, final int blockX, final int blockZ) {
        if (!world.getEnvironment().equals(World.Environment.NORMAL) ||
                ((long) blockX * (long) blockX) + ((long) blockZ * (long) blockZ) < 400000000L
        ) {
            return null;
        }
        return new GenerationInfo(0.01, Math.max(world.getMinHeight(), world.getMaxHeight() - 20));
    }
}
