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

package com.andrewreedhall.ancientscrolls.flask;

import com.andrewreedhall.ancientscrolls.Utils;
import org.bukkit.Location;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Random;

public class FlaskGenerationListener implements Listener {
    public FlaskGenerationListener() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLootGenerate(LootGenerateEvent event) {
        InventoryHolder inventoryHolder = event.getInventoryHolder();
        if (!(inventoryHolder instanceof Chest || inventoryHolder instanceof Barrel || inventoryHolder instanceof StorageMinecart)) {
            return;
        }
        Location location = Utils.getLootGenerateEventLocation(event);
        if (location == null) {
            return;
        }
        Random random = Utils.createRandomBasedOnLocation(event.getWorld(), location, Utils.NULL_SALT);
        if (random.nextBoolean()) {
            return;
        }
        Flask[] flasks = Flask.values();
        event.getLoot().add(flasks[random.nextInt(flasks.length)].createItemStack());
    }
}
