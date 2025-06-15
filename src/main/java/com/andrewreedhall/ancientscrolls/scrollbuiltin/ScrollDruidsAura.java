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

import com.andrewreedhall.ancientscrolls.Utils;
import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollDruidsAura extends Scroll {
    private static final double RADIUS = 3.0;
    private static final double RADIUS_SQUARED = RADIUS * RADIUS;
    private static final Particle[] PARTICLES = new Particle[] {
            Particle.COMPOSTER,
            Particle.TRIAL_OMEN,
            Particle.NAUTILUS
    };

    public ScrollDruidsAura() {
        super(
                NamespacedKey.fromString("druidsaura", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Druid's Aura",

                "Weakens all monsters within 3 blocks"
        );
        this.scheduledTickPeriod = 5L;
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.MANSION),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.VINDICATOR)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        Location location = player.getLocation();
        player.getNearbyEntities(RADIUS, RADIUS, RADIUS).forEach((Entity entity) -> {
            if (!Utils.isEntityNonBossMonster(entity)) {
                return;
            }
            Monster monster = (Monster) entity;
            if (monster.hasPotionEffect(PotionEffectType.WEAKNESS) || monster.getLocation().distanceSquared(location) > RADIUS_SQUARED) {
                return;
            }
            monster.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 140, 1, false, true));
        });
        // particle
        Random random = new Random();
        player.getWorld().spawnParticle(
                PARTICLES[random.nextInt(PARTICLES.length)],
                location,
                1,
                RADIUS,
                RADIUS,
                RADIUS,
                0.1
        );
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}
}
