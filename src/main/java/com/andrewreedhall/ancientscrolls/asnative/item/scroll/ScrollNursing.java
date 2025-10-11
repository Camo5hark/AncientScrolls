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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ScrollNursing extends ItemScrollNative {
    private static final double RADIUS = 5.0;
    private static final double RADIUS_SQUARED = RADIUS * RADIUS;
    private static final int N_PARTICLES = 12;
    private static final double PARTICLE_ANGLE_STEP = (2.0 * Math.PI) / N_PARTICLES;

    public ScrollNursing() {
        super("nursing", "Nursing", new String[] {
                "Regenerates players and villagers within 5 blocks"
        });
        this.putMCLootTableGenProb("chests/stronghold_crossing", 0.186);
        this.putMCLootTableGenProb("chests/ancient_city", 0.084);
        this.putMCLootTableGenProb("chests/trial_chambers/intersection", 0.112);
        this.ominousVaultGenProb = 0.075;
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final World equippingPlayerWorld = equippingPlayer.getWorld();
            final Location equippingPlayerLocation = equippingPlayer.getLocation();
            equippingPlayerWorld
                    .getNearbyLivingEntities(equippingPlayerLocation, RADIUS)
                    .stream()
                    .filter(
                            (final LivingEntity nearbyLivingEntity) ->
                                    !nearbyLivingEntity.equals(equippingPlayer) &&
                                    (nearbyLivingEntity instanceof Player || nearbyLivingEntity instanceof Villager) &&
                                    nearbyLivingEntity.getLocation().distanceSquared(equippingPlayerLocation) <= RADIUS_SQUARED
                    )
                    .forEach(
                            (final LivingEntity nearbyLivingEntity) ->
                                    nearbyLivingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 25, 0, false))
                    );
            final double particleX = equippingPlayerLocation.getX();
            final double particleY = equippingPlayerLocation.getY() + 0.75;
            final double particleZ = equippingPlayerLocation.getZ();
            for (int i = 0; i < N_PARTICLES; ++i) {
                double particleRelX = RADIUS * Math.cos(PARTICLE_ANGLE_STEP * i);
                double particleRelZ = RADIUS * Math.sin(PARTICLE_ANGLE_STEP * i);
                equippingPlayerWorld.spawnParticle(Particle.HEART, particleX + particleRelX, particleY, particleZ + particleRelZ, 1);
            }
        }, 20L);
    }
}
