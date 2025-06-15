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

    License file: COPYING
    Email for contact: camo5hark10@gmail.com

 */

package com.andrewreedhall.ancientscrolls.scroll.generation;

import com.andrewreedhall.ancientscrolls.Utils;
import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.InventoryHolder;

public class ScrollGeneratorTreasure extends ScrollGenerator {
    private final Class<? extends InventoryHolder> treasureContainerType;
    private final Structure structure;

    public ScrollGeneratorTreasure(double probability, Class<? extends InventoryHolder> treasureContainerType, Structure structure) {
        super(probability);
        this.treasureContainerType = treasureContainerType;
        this.structure = structure;
    }

    @Override
    public void generate(Event event, Scroll scroll) {
        if (!(event instanceof LootGenerateEvent lootGenerateEvent)) {
            return;
        }
        InventoryHolder inventoryHolder = lootGenerateEvent.getInventoryHolder();
        if (!this.treasureContainerType.isInstance(inventoryHolder)) {
            return;
        }
        Location location = Utils.getLootGenerateEventLocation(lootGenerateEvent);
        if (location == null) {
            return;
        }
        // make sure container is highly likely to be in correct structure
        Chunk chunk = location.getChunk();
        if (!chunk.isLoaded() || !chunk.isGenerated() || chunk.getStructures(this.structure).isEmpty()) {
            return;
        }
        // repeatable/procedural generation based on world seed
        if (Utils.createRandomBasedOnLocation(lootGenerateEvent.getWorld(), location, scroll.hashCode()).nextDouble() > this.probability) {
            return;
        }
        lootGenerateEvent.getLoot().add(scroll.createItemStack());
    }
}
