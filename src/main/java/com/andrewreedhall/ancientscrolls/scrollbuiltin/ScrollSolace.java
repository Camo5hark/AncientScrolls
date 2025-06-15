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

import com.andrewreedhall.ancientscrolls.CacheMap;
import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.ScrollScheduler;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.generator.structure.Structure;

import java.util.UUID;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollSolace extends Scroll {
    private static final CacheMap<UUID, Long> PLAYER_LAST_DAMAGE_TICK = CacheMap.createForOnlinePlayers();

    public ScrollSolace() {
        super(
                NamespacedKey.fromString("solace", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Solace",

                "Invincible to attacks for 1.25 seconds after being attacked"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.FORTRESS),
                new ScrollGeneratorTreasure(0.1, Chest.class, Structure.RUINED_PORTAL)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        UUID playerUID = player.getUniqueId();
        Long playerLastDamageTick = PLAYER_LAST_DAMAGE_TICK.get(playerUID);
        if (playerLastDamageTick == null) {
            playerLastDamageTick = 0L;
        }
        if (ScrollScheduler.ticks() - playerLastDamageTick < 25L) {
            event.setCancelled(true);
            return;
        }
        PLAYER_LAST_DAMAGE_TICK.put(playerUID, ScrollScheduler.ticks());
    }
}
