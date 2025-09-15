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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollResistance extends ItemScrollNative implements Listener {
    public ScrollResistance() {
        super("resistance", "Resistance", new String[] {
                "Negates attack knockback"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/warden", 1.0);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !this.isEquipping(damagedPlayer)) {
            return;
        }
        plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(
                plugin(),
                () -> damagedPlayer.setVelocity(new Vector(0.0, 0.0, 0.0))
        ));
    }
}
