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

package com.andrewreedhall.ancientscrolls.util;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Simulates poison effects on monsters (who are immune to poison effects in vanilla)
 */
public final class MonsterPoisonSimulator {
    private static class MonsterPoisonInstance {
        public final Monster monster;
        public int ttl;

        public MonsterPoisonInstance(final Monster monster, final int ttl) {
            this.monster = monster;
            this.ttl = ttl;
        }

        @Override
        public int hashCode() {
            return this.monster.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return obj == this || (obj instanceof MonsterPoisonSimulator.MonsterPoisonInstance && obj.hashCode() == this.hashCode());
        }
    }

    private final Set<MonsterPoisonInstance> monsterPoisonInstances = new HashSet<>();

    /**
     * Creates a new poison simulator.
     */
    public MonsterPoisonSimulator() {}

    /**
     * Schedules a repeating task to apply poison damage and particles.
     */
    public void scheduleRepeatingTask() {
        plugin().scheduleTask(
                (final BukkitScheduler scheduler) ->
                        scheduler.scheduleSyncRepeatingTask(
                                plugin(),
                                () -> {
                                    this.monsterPoisonInstances.removeIf((final MonsterPoisonInstance monsterPoisonInstance) -> monsterPoisonInstance.ttl <= 0);
                                    for (final MonsterPoisonInstance monsterPoisonInstance : this.monsterPoisonInstances) {
                                        monsterPoisonInstance.ttl -= 20;
                                        monsterPoisonInstance.monster.damage(1.0);
                                        monsterPoisonInstance.monster.getWorld().spawnParticle(
                                                Particle.ENTITY_EFFECT,
                                                monsterPoisonInstance.monster.getEyeLocation(),
                                                10,
                                                0.5,
                                                0.5,
                                                0.5,
                                                Color.fromRGB(0x87A363)
                                        );
                                    }
                                },
                                0L,
                                20L
                        )
        );
    }

    /**
     * Applies poison to a living entity.
     * <ul>
     *     <li>If it's a monster, applies custom poison logic.</li>
     *     <li>Otherwise, applies standard poison effect.</li>
     * </ul>
     * @param livingEntity the entity to poison
     * @param duration poison duration in ticks
     */
    public void add(final LivingEntity livingEntity, final int duration) {
        if (livingEntity instanceof Monster monster) {
            this.monsterPoisonInstances.add(new MonsterPoisonInstance(monster, duration));
        } else {
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 0, false));
        }
    }
}
