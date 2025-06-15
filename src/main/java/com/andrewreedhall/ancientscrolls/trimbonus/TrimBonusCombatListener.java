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

package com.andrewreedhall.ancientscrolls.trimbonus;

import com.andrewreedhall.ancientscrolls.OriginalEntityDamageListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class TrimBonusCombatListener implements Listener {
    public TrimBonusCombatListener() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamagedByEntity(EntityDamageByEntityEvent event) {
        if (!plugin().getCachedConfig().trimBonus_enabled) {
            return;
        }
        Entity victimEntity = event.getEntity();
        if (!(victimEntity instanceof Player victimPlayer) || !TrimBonus.doesPlayerHaveBonusAgainst(victimPlayer, event.getDamager().getType())) {
            return;
        }
        modifyDamage(event, -plugin().getCachedConfig().trimBonus_damageReductionPercentage);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamagedByPlayer(EntityDamageByEntityEvent event) {
        if (!plugin().getCachedConfig().trimBonus_enabled) {
            return;
        }
        Entity attackingEntity = event.getDamager();
        if (!(attackingEntity instanceof Player attackingPlayer) || !TrimBonus.doesPlayerHaveBonusAgainst(attackingPlayer, event.getEntity().getType())) {
            return;
        }
        modifyDamage(event, plugin().getCachedConfig().trimBonus_damageInflictionPercentage);
    }

    private static void modifyDamage(EntityDamageByEntityEvent event, double scalarPercentage) {
        double originalDamage = OriginalEntityDamageListener.getOriginalDamage(event);
        double damageAddend = originalDamage * scalarPercentage * 0.01;
        double damage = originalDamage + damageAddend;
        event.setDamage(damage);
    }
}
