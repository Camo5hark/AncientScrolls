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
import com.andrewreedhall.ancientscrolls.Entropic;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Random;

public abstract class AncientScrollsStructure extends AncientScrollsRegistry.Value implements Entropic {
    protected record GenerationInfo(double prob, int offsetBlockX, int blockY, int offsetBlockZ) {}

    private final long entropy;

    public AncientScrollsStructure(final NamespacedKey key) {
        super(key);
        this.entropy = this.generateEntropy();
    }

    protected abstract GenerationInfo createGenerationInfo(final Chunk chunk);

    public boolean generate(final ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        final GenerationInfo generationInfo = this.createGenerationInfo(chunk);
        if (generationInfo == null) {
            return false;
        }
        StructureGenerator.generate(
                this,
                ((CraftWorld) chunk.getWorld()).getHandle(),
                (chunk.getX() << 4) + generationInfo.offsetBlockX(),
                generationInfo.blockY(),
                (chunk.getZ() << 4) + generationInfo.offsetBlockZ()
        );
        return true;
    }

    private Random createChunkBasedRandom(final Chunk chunk) {
        long x = (chunk.getX() & 0x18) << 24;
        long z = chunk.getZ() & 0x18;
        long seed = (x | z) & this.entropy;
        return new Random(seed);
    }
}
