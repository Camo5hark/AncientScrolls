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

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public final class ScrollCanineStudies extends ItemScrollNative implements Listener {
    public ScrollCanineStudies() {
        super("canine_studies", "Canine Studies", new String[] {
                "Night vision at night",
                "+20% damage to skeletons"
        });
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("entities/wither_skeleton", 0.01);
        this.addPotionEffectToEquippingPlayers(NIGHT_VISION_POTION_EFFECT, (final Player equippingPlayer) -> !equippingPlayer.getWorld().isDayTime());
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof AbstractSkeleton) || !(BukkitUtil.getDamager(event) instanceof Player playerDamager) || !this.isEquipping(playerDamager)) {
            return;
        }
        event.setDamage(event.getDamage() + (event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) * 0.2));
    }
}
