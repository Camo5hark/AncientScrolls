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

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class StructurePlacer {
    public static void place(
            final AncientScrollsStructure structure,
            final ServerLevel level,
            final int blockX,
            final int blockY,
            final int blockZ
    ) {
        final StructureTemplateManager levelStructureTemplateManager = level.getStructureManager();
        final String structureKeyString = structure.key.toString();
        final Optional<StructureTemplate> optionalStructureTemplate = levelStructureTemplateManager.get(
                ResourceLocation.parse(structureKeyString)
        );
        if (optionalStructureTemplate.isEmpty()) {
            plugin().getLogger().warning("Structure template not found: " + structureKeyString);
            return;
        }
        optionalStructureTemplate.get().placeInWorld(
                level,
                BlockPos.ZERO,
                new BlockPos(blockX, blockY, blockZ),
                new StructurePlaceSettings()
                        .setIgnoreEntities(false)
                        .setRotation(Rotation.NONE)
                        .setMirror(Mirror.NONE)
                        .setLiquidSettings(LiquidSettings.APPLY_WATERLOGGING),
                level.getRandom(),
                2
        );
    }
}
