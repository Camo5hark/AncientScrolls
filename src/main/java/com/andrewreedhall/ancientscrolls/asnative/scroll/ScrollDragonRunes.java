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
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ScrollDragonRunes extends ItemScrollNative implements Listener {
    public ScrollDragonRunes() {
        super("dragon_runes", "Dragon Runes", new String[] {
                "Negates dragon's breath damage",
                "Drop lingering dragon's breath cloud on attackers"
        });
        this.enderDragonReward = true;
        this.special = true;
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !this.isEquipping(damagedPlayer)) {
            return;
        }
        final Entity damagingEntity = event.getDamager();
        if (damagingEntity instanceof AreaEffectCloud damagingCloud && damagingCloud.hasCustomEffect(PotionEffectType.INSTANT_DAMAGE)) {
            event.setCancelled(true);
        } else if (damagingEntity instanceof LivingEntity damagingLivingEntity) {
            final World damagingLivingEntityWorld = damagingLivingEntity.getWorld();
            final AreaEffectCloud dragonsBreathCloud = damagingLivingEntityWorld.spawn(damagingEntity.getLocation(), AreaEffectCloud.class);
            dragonsBreathCloud.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 100, 0), true);
            dragonsBreathCloud.setDuration(100);
            dragonsBreathCloud.setRadius(3.0F);
            dragonsBreathCloud.setColor(Color.FUCHSIA);
            damagingLivingEntityWorld.playSound(damagingEntity, Sound.ENTITY_ENDER_DRAGON_HURT, 1.0F, 0.75F);
        }
    }
}
