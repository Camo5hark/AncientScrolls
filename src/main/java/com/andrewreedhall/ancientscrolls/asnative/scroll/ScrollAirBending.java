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
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollAirBending extends ItemScrollNative implements Listener {
    public ScrollAirBending() {
        super("air_bending", "Air Bending", new String[] {
                "50% chance to cast wind charge explosion on targets attacked with projectiles"
        });
        this.putMCLootTableGenProb("entities/breeze", 0.01);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile)) {
            return;
        }
        final Entity damager = BukkitUtil.getDamager(event);
        if (!(damager instanceof Player damagerPlayer) || !this.isEquipping(damagerPlayer) || plugin().getUniversalRandom().nextBoolean()) {
            return;
        }
        final Entity damaged = event.getEntity();
        damaged.getWorld().spawn(damaged.getLocation(), WindCharge.class).explode();
        damagerPlayer.playSound(damagerPlayer, Sound.ENTITY_WIND_CHARGE_THROW, 1.0F, 1.0F);
    }
}
