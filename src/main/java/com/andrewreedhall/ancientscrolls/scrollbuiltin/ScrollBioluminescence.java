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

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollBioluminescence extends Scroll {
    private static final Set<UUID> AFFECTED_PLAYERS = new HashSet<>();

    public ScrollBioluminescence() {
        super(
                NamespacedKey.fromString("bioluminescence", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER | Scroll.FLAG_SCHEDULE_PER_UNAFFECTED_PLAYER,
                "Bioluminescence",

                "Gives user an aura of light"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.05, EntityType.PHANTOM),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.BLAZE),
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.JUNGLE_PYRAMID)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        AFFECTED_PLAYERS.add(player.getUniqueId());
        double simDist = player.getWorld().getSimulationDistance();
        double simDistSquared = simDist * simDist;
        Location playerBlockLocation = player.getEyeLocation().getBlock().getLocation();
        Light light = (Light) Material.LIGHT.createBlockData();
        light.setLevel(15);
        BukkitScheduler scheduler = plugin().getServer().getScheduler();
        plugin().getServer().getOnlinePlayers().forEach((Player onlinePlayer) -> {
            // only send block update to nearby players
            if (onlinePlayer.getLocation().distanceSquared(playerBlockLocation) >= simDistSquared) {
                return;
            }
            onlinePlayer.sendBlockChange(playerBlockLocation, light);
            scheduler.scheduleSyncDelayedTask(plugin(), () -> {
                // possibility player dc'd within delay
                if (!onlinePlayer.isOnline()) {
                    return;
                }
                Location latePlayerBlockLocation = player.getEyeLocation().getBlock().getLocation();
                if (latePlayerBlockLocation.equals(playerBlockLocation)) {
                    return;
                }
                onlinePlayer.sendBlockChange(playerBlockLocation, Material.AIR.createBlockData());
            }, 10L);
        });
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {
        UUID playerUID = player.getUniqueId();
        if (!AFFECTED_PLAYERS.contains(playerUID)) {
            return;
        }
        AFFECTED_PLAYERS.remove(playerUID);
        player.sendBlockChange(player.getEyeLocation().getBlock().getLocation(), Material.AIR.createBlockData());
    }
}
