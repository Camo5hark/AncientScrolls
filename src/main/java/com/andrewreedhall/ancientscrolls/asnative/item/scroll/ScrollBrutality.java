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

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitScheduler;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollBrutality extends ItemScrollNative implements Listener {
    public ScrollBrutality() {
        super("brutality", "Brutality", new String[] {
                "Increases melee knockback",
                "Critical melee attacks launch target into the air"
        });
        this.putMCLootTableGenProb("entities/hoglin", 0.005);
        this.putMCLootTableGenProb("entities/piglin_brute", 0.01);
        this.putMCLootTableGenProb("chests/ancient_city", 0.084);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity damager = BukkitUtil.getDamager(event);
        if (!(damager instanceof final Player damagerPlayer) || !this.isEquipping(damagerPlayer)) {
            return;
        }
        final Entity damaged = event.getEntity();
        double damagedYVel;
        final World damagedWorld = damaged.getWorld();
        if (BukkitUtil.canCrit(damagerPlayer)) {
            damagedYVel = 0.6;
            damagedWorld.playSound(damaged, Sound.BLOCK_ANVIL_PLACE, 0.75F, 0.25F);
        } else {
            damagedYVel = 0.1;
        }
        damagedWorld.spawnParticle(Particle.DUST, damaged.getLocation(), 50, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.MAROON, 1.5F));
        damaged.teleport(damaged.getLocation().add(0.0, 0.1, 0.0));
        plugin().scheduleTask(
                (final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(
                        plugin(),
                        () -> damaged.setVelocity(
                                damaged.getVelocity().add(damagerPlayer.getLocation().getDirection().multiply(1.25).setY(damagedYVel))
                        )
                )
        );
    }
}
