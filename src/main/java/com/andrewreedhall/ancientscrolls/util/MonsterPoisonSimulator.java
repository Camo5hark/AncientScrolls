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

    public MonsterPoisonSimulator() {}

    public void scheduleRepeatingTask() {
        plugin().scheduleTask(
                (final BukkitScheduler scheduler) ->
                        scheduler.scheduleSyncRepeatingTask(
                                plugin(),
                                () -> {
                                    this.monsterPoisonInstances.removeIf((final MonsterPoisonInstance monsterPoisonInstance) -> monsterPoisonInstance.ttl <= 0);
                                    this.monsterPoisonInstances.forEach((final MonsterPoisonInstance monsterPoisonInstance) -> {
                                        monsterPoisonInstance.ttl -= 20;
                                        monsterPoisonInstance.monster.damage(1.0);
                                        monsterPoisonInstance.monster.getWorld().spawnParticle(
                                                Particle.ENTITY_EFFECT,
                                                monsterPoisonInstance.monster.getEyeLocation(),
                                                10,
                                                0.5,
                                                0.5,
                                                0.5,
                                                Color.fromRGB(0x00FF00)
                                        );
                                    });
                                },
                                0L,
                                20L
                        )
        );
    }

    public void add(final LivingEntity livingEntity, final int duration) {
        if (livingEntity instanceof Monster monster) {
            this.monsterPoisonInstances.add(new MonsterPoisonInstance(monster, duration));
        } else {
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 0, false));
        }
    }
}
