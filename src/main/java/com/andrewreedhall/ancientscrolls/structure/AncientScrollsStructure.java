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

import com.andrewreedhall.ancientscrolls.AncientScrollsRegistry;
import com.andrewreedhall.ancientscrolls.util.Entropic;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Random;

public abstract class AncientScrollsStructure extends AncientScrollsRegistry.Value implements Entropic, StructureTemplateAccess {
    public record GenerationInfo(double prob, int blockY) {}

    public final StructureTemplate structureTemplate = new StructureTemplate();
    private final long entropy;

    public AncientScrollsStructure(final NamespacedKey key) {
        super(key);
        this.entropy = this.generateEntropy();
        this.load();
    }

    protected abstract GenerationInfo createGenerationInfo(final World world, final int blockX, final int blockZ);

    @Override
    public AncientScrollsStructure getStructure() {
        return this;
    }

    public boolean generate(final ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        final GenerationInfo generationInfo = this.createGenerationInfo(chunk.getWorld(), chunk.getX() << 4, chunk.getZ() << 4);
        final Random random = this.generateRandom(this.entropy, chunk.getWorld().getSeed(), chunk.getX(), chunk.getZ());
        if (generationInfo == null || random.nextDouble() > generationInfo.prob) {
            return false;
        }
        this.place(
                ((CraftWorld) chunk.getWorld()).getHandle(),
                chunk.getX() << 4,
                generationInfo.blockY(),
                chunk.getZ() << 4,
                random
        );
        return true;
    }
}
