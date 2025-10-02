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
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.event.world.ChunkLoadEvent;

public abstract class AncientScrollsStructure extends AncientScrollsRegistry.Value {
    public AncientScrollsStructure(final NamespacedKey key) {
        super(key);
    }

    protected abstract boolean canGenerate(final Chunk chunk);

    public boolean generate(final ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        if (!this.canGenerate(chunk)) {
            return false;
        }
        // generate
        return true;
    }
}
