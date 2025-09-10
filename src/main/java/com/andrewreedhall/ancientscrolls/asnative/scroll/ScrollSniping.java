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

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class ScrollSniping extends ItemScrollNative implements Listener {
    public ScrollSniping() {
        super("sniping", "Sniping", new String[] {
                "Arrow hitscan"
        });
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.21);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow launchedArrow) || !(launchedArrow.getShooter() instanceof Player launchingPlayer) || !this.isEquipping(launchingPlayer)) {
            return;
        }
        final World launchingPlayerWorld = launchingPlayer.getWorld();
        final Vector launchingPlayerDirection = launchingPlayer.getLocation().getDirection();
        final RayTraceResult rayTraceResult = launchingPlayerWorld.rayTraceEntities(
                launchingPlayer.getEyeLocation(),
                launchingPlayerDirection,
                (launchingPlayerWorld.getSimulationDistance() - 1) * 16.0,
                1.0,
                (final Entity rayTracedEntity) ->
                        rayTracedEntity instanceof LivingEntity && launchedArrow.canHitEntity(rayTracedEntity)
        );
        if (rayTraceResult == null) {
            return;
        }
        final LivingEntity hitLivingEntity = (LivingEntity) rayTraceResult.getHitEntity();
        if (hitLivingEntity == null) {
            return;
        }
        launchedArrow.hitEntity(hitLivingEntity, launchingPlayerDirection);
        launchingPlayerWorld.playSound(launchingPlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        hitLivingEntity.getWorld().spawnParticle(Particle.DUST, hitLivingEntity.getEyeLocation(), 50, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.MAROON, 1.5F));
    }
}
