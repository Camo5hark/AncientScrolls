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

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollToxicology extends ItemScrollNative implements Listener {
    public ScrollToxicology() {
        super("toxicology", "Toxicology", new String[] {
                "Negates poison damage",
                "Poison melee targets when poisoned"
        });
        this.putMCLootTableGenProb("entities/cave_spider", 0.005);
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("entities/witch", 0.01);
        this.putMCLootTableGenProb("chests/jungle_temple", 0.127);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) ||
                !event.getCause().equals(EntityDamageEvent.DamageCause.POISON) ||
                !this.isEquipping(damagedPlayer)
        ) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity damagedLivingEntity) ||
                !(event.getDamager() instanceof Player damagingPlayer) ||
                !damagingPlayer.hasPotionEffect(PotionEffectType.POISON) ||
                !this.isEquipping(damagingPlayer)
        ) {
            return;
        }
        plugin().getMonsterPoisonSimulator().add(damagedLivingEntity, 80);
    }
}
