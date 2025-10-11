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

import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollDeflection extends ItemScrollNative implements Listener {
    public ScrollDeflection() {
        super("deflection", "Deflection", new String[] {
                "40% chance arrows will be deflected towards shooter"
        });
        this.putMCLootTableGenProb("chests/jungle_temple", 0.086);
        this.putMCLootTableGenProb("chests/pillager_outpost", 0.11);
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/pillager", 0.01);
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow hittingArrow) ||
                !(hittingArrow.getShooter() instanceof Entity shooter) ||
                !(event.getHitEntity() instanceof Player hitPlayer) ||
                !this.isEquipping(hitPlayer) ||
                plugin().getUniversalRandom().nextDouble() > 0.4
        ) {
            return;
        }
        event.setCancelled(true);
        hittingArrow.remove();
        hitPlayer.launchProjectile(hittingArrow.getClass(), shooter.getLocation().subtract(hitPlayer.getLocation()).toVector().multiply(0.25));
        hitPlayer.getWorld().playSound(hitPlayer, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 1.0F);
    }
}
