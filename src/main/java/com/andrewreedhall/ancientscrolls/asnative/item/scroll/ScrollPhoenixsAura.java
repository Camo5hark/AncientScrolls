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

public final class ScrollPhoenixsAura extends ItemScrollNative implements Listener {
    public ScrollPhoenixsAura() {
        super("phoenixs_aura", "Phoenix's Aura", new String[] {
                "50% chance to start attackers on fire",
                "Negates fire spreading from flaming melee attackers"
        });
        this.putMCLootTableGenProb("chests/nether_bridge", 0.274);
        this.putMCLootTableGenProb("entities/magma_cube", 0.005);
        this.putMCLootTableGenProb("entities/blaze", 0.01);
        this.scheduleRepeatingTaskPerEquippingPlayer(
                (final Player equippingPlayer) ->
                        equippingPlayer.getWorld().spawnParticle(
                                Particle.FLAME,
                                equippingPlayer.getEyeLocation(),
                                5,
                                5.0,
                                5.0,
                                5.0,
                                0.1
                        ),
                5L
        );
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !this.isEquipping(damagedPlayer) || plugin().getUniversalRandom().nextBoolean()) {
            return;
        }
        final Entity damager = BukkitUtil.getDamager(event);
        damager.setFireTicks(160);
        final World damagerWorld = damager.getWorld();
        damagerWorld.playSound(damager, Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        damagerWorld.spawnParticle(Particle.FLAME, damager.getLocation(), 30, 2.0, 2.0, 2.0, 0.5);
        plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(
                plugin(),
                () -> damagedPlayer.setFireTicks(0)
        ));
    }
}
