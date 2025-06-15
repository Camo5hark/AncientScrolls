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

package com.andrewreedhall.ancientscrolls;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class OriginalEntityDamageListener implements Listener {
    private static final CacheMap<EntityDamageEvent, Double> CACHE = new CacheMap<>(() -> plugin().getCachedConfig().eventCacheCapacity);

    public OriginalEntityDamageListener() {}

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        CACHE.put(event, event.getDamage());
    }

    public static double getOriginalDamage(EntityDamageEvent event) {
        Double originalDamage = CACHE.get(event);
        // return current damage as decent estimate of original damage if event was dropped by this listener
        return originalDamage == null ? event.getDamage() : originalDamage;
    }
}
