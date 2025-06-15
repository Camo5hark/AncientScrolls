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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Random;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollDeflection extends Scroll {
    public ScrollDeflection() {
        super(
                NamespacedKey.fromString("deflection", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Deflection",

                "40% chance projectiles will be launched back at attackers"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.2, Chest.class, Structure.JUNGLE_PYRAMID),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.SKELETON),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.STRAY),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.BOGGED)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof Player player) || !this.doesPlayerHaveScroll(player) || new Random().nextDouble() > 0.4) {
            return;
        }
        event.setCancelled(true);
        Projectile projectile = event.getEntity();
        ProjectileSource projectileSource = projectile.getShooter();
        if (!(projectileSource instanceof Entity entity)) {
            return;
        }
        event.setCancelled(true);
        projectile.remove();
        player.launchProjectile(projectile.getClass(), entity.getLocation().subtract(player.getLocation()).toVector().multiply(0.5));
        // play shoot sound
        // magic particles
//        Vector velocity = !(projectileSource instanceof Entity entity) ? projectile.getVelocity().multiply(-1.0) : entity.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(5.0);
//        projectile = (Projectile) projectile.copy();
//        projectile.setShooter(player);
//        projectile.setVelocity(velocity);
    }
}
