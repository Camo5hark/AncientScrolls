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

package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public final class ScrollNocturnal extends ItemScrollNative implements Listener {
    public ScrollNocturnal() {
        super("nocturnal", "Nocturnal", new String[] {
                "Prevents phantoms from spawning nearby"
        });
        this.putMCLootTableGenProb("entities/phantom", 0.01);
        this.vaultGenProb = 0.104;
    }

    @EventHandler
    public void onEntitySpawn(final EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Phantom spawnedPhantom)) {
            return;
        }
        final boolean nearbyPlayerIsEquipping = spawnedPhantom
                .getWorld()
                .getNearbyEntitiesByType(Player.class, spawnedPhantom.getLocation(), 10.0, 34.0, 10.0)
                .stream()
                .anyMatch(this::isEquipping);
        if (!nearbyPlayerIsEquipping) {
            return;
        }
        event.setCancelled(true);
    }
}
