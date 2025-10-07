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

import com.andrewreedhall.ancientscrolls.util.Randomizer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class StructureListener implements Listener {
    private static final Randomizer<AncientScrollsStructure> STRUCTURE_RANDOMIZER = new Randomizer<>();

    public StructureListener() {}

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (!plugin().getDefaultCachedConfig().structure_generation_enabled || !event.isNewChunk()) {
            return;
        }
        final List<AncientScrollsStructure> registeredStructures = new ArrayList<>(plugin().getStructureRegistry().getAll());
        STRUCTURE_RANDOMIZER.sort(registeredStructures, new Random(event.getChunk().getChunkKey()));
        for (final AncientScrollsStructure structure : registeredStructures) {
            if (structure.generate(event)) {
                break;
            }
        }
    }
}
