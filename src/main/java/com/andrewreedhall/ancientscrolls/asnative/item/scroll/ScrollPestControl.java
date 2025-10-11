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

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public final class ScrollPestControl extends ItemScrollNative implements Listener {
    private static final Set<EntityType> ENTITY_TYPES = Set.of(
            EntityType.SPIDER,
            EntityType.SILVERFISH,
            EntityType.ENDERMITE
    );

    public ScrollPestControl() {
        super("pest_control", "Pest Control", new String[] {
                "Negates:",
                "- infested effect",
                "- damage from regular spiders, silverfish, and endermites"
        });
        this.putMCLootTableGenProb("entities/spider", 0.005);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.INFESTED);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !ENTITY_TYPES.contains(event.getDamager().getType()) || !this.isEquipping(damagedPlayer)) {
            return;
        }
        event.setCancelled(true);
    }
}
