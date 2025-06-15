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

package com.andrewreedhall.ancientscrolls.scrollbuiltin;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollNursing extends Scroll {
    private static final int N_PARTICLES = 20;
    private static final double RADIUS = 5.0;
    private static final double RADIUS_SQUARED = RADIUS * RADIUS;
    private static final double PARTICLE_ANGLE_STEP = (2.0 * Math.PI) / (double) N_PARTICLES;

    public ScrollNursing() {
        super(
                NamespacedKey.fromString("nursing", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Nursing",

                "Grants regeneration to players and villagers within 5 blocks",
                "Does NOT grant regeneration to user"
        );
        this.scheduledTickPeriod = 50L;
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.05, EntityType.WITCH),
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.STRONGHOLD)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        UUID playerUID = player.getUniqueId();
        Location playerLocation = player.getEyeLocation();
        player.getNearbyEntities(RADIUS, RADIUS, RADIUS).stream().filter((Entity nearbyEntity) ->
            !nearbyEntity.getUniqueId().equals(playerUID)
            && (nearbyEntity instanceof Player || nearbyEntity instanceof Villager)
            && nearbyEntity.getLocation().distanceSquared(playerLocation) <= RADIUS_SQUARED
        ).forEach((Entity nearbyPlayerOrVillager) -> {
            ((LivingEntity) nearbyPlayerOrVillager).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) this.scheduledTickPeriod, 1, false, true));
        });
        // heart particle ring
        World world = player.getWorld();
        Location particleCenter = player.getLocation();
        double particleY = particleCenter.getY() + 0.8;
        for (int i = 0; i < N_PARTICLES; i++) {
            double x = RADIUS * (Math.cos(PARTICLE_ANGLE_STEP * i));
            double z = RADIUS * (Math.sin(PARTICLE_ANGLE_STEP * i));
            //System.out.println(x + " " + z);
            world.spawnParticle(Particle.HEART, particleCenter.getX() + x, particleY, particleCenter.getZ() + z, 1);
        }
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}
}
