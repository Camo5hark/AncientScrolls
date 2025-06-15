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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollMartyrdom extends Scroll {
    public ScrollMartyrdom() {
        super(
                NamespacedKey.fromString("martyrdom", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Martyrdom",

                "8% chance of casting small explosion upon attackers",
                "Large explosion upon your death"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.02, EntityType.CREEPER),
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.DESERT_PYRAMID)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!this.doesPlayerHaveScroll(player)) {
            return;
        }
        player.getWorld().createExplosion(player.getLocation(), 4.0F);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player) || !this.doesPlayerHaveScroll(player) || new Random().nextDouble() > 0.08) {
            return;
        }
        Entity damager = event.getDamager();
        if (damager instanceof Projectile damagerProjectile && damagerProjectile.getShooter() instanceof Entity shooter) {
            damager = shooter;
        }
        player.getWorld().createExplosion(damager.getLocation(), 1.25F);
        if (player.getLocation().distanceSquared(damager.getLocation()) <= 16.0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0));
        }
    }
}
