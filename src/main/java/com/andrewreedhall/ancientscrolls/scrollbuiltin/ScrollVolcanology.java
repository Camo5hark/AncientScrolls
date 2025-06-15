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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.generator.structure.Structure;

import java.util.UUID;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollVolcanology extends Scroll {
    private static final CacheMap<UUID, Long> PLAYER_LAVA_IMMUNITY_TICKS = CacheMap.createForOnlinePlayers();

    public ScrollVolcanology() {
        super(
                NamespacedKey.fromString("volcanology", plugin()),
                Scroll.FLAG_REGISTER_EVENTS | Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Volcanology",

                "Grants immunity to lava for 10 seconds"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.025, EntityType.MAGMA_CUBE),
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.FORTRESS)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        if (player.getLocation().getBlock().getType().equals(Material.LAVA)) {
            return;
        }
        PLAYER_LAVA_IMMUNITY_TICKS.put(player.getUniqueId(), 200L);
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || !event.getDamageSource().getDamageType().equals(DamageType.LAVA) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        UUID playerUID = player.getUniqueId();
        Long playerLastDamageTick = PLAYER_LAVA_IMMUNITY_TICKS.get(playerUID);
        if (playerLastDamageTick == null) {
            playerLastDamageTick = 200L;
        }
        if (playerLastDamageTick <= 0L) {
            return;
        }
        event.setCancelled(true);
        player.setFireTicks(0);
        PLAYER_LAVA_IMMUNITY_TICKS.put(playerUID, playerLastDamageTick - 1);
    }
}
