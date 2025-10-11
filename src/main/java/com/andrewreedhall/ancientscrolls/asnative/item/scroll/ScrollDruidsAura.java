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

package com.andrewreedhall.ancientscrolls.asnative.item.scroll;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public final class ScrollDruidsAura extends ItemScrollNative {
    public ScrollDruidsAura() {
        super("druids_aura", "Druid's Aura", new String[] {
                "Slowly damages monsters within a 3 block radius"
        });
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final World equippingPlayerWorld = equippingPlayer.getWorld();
            final Location equippingPlayerLocation = equippingPlayer.getLocation();
            equippingPlayerWorld
                    .getNearbyEntitiesByType(Monster.class, equippingPlayer.getLocation(), 3.0)
                    .stream()
                    .filter((final Monster nearbyMonster) -> !(nearbyMonster instanceof Boss) && nearbyMonster.getLocation().distanceSquared(equippingPlayerLocation) <= 9.0)
                    .forEach((final Monster nearbyMonster) -> {
                        nearbyMonster.damage(1.0);
                        equippingPlayerWorld.spawnParticle(Particle.HAPPY_VILLAGER, equippingPlayer.getEyeLocation(), 10, 3.0, 3.0, 3.0);
                    });
        }, 20L);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.5);
    }
}
