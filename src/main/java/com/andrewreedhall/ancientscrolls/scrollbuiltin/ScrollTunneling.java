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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.RayTraceResult;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollTunneling extends Scroll {
    public ScrollTunneling() {
        super(
                NamespacedKey.fromString("tunneling", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Tunneling",

                "Grants ability to teleport instantly with ender pearls"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.END_CITY)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl enderPearl) || !(enderPearl.getShooter() instanceof Player player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        RayTraceResult rayTraceResult = player.rayTraceBlocks(Math.max(8, (plugin().getServer().getSimulationDistance() - 1) * 16), FluidCollisionMode.NEVER);
        if (rayTraceResult == null) {
            event.setCancelled(true);
            player.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
            return;
        }
        Block hitBlock = rayTraceResult.getHitBlock();
        BlockFace hitBlockFace = rayTraceResult.getHitBlockFace();
        if (hitBlock == null || hitBlockFace == null) {
            event.setCancelled(true);
            return;
        }
        plugin().getServer().getScheduler().scheduleSyncDelayedTask(plugin(), () -> {
            enderPearl.remove();
            player.setCooldown(Material.ENDER_PEARL, 60);
        }, 1L);
        Location teleportBlockLocation = hitBlock.getRelative(hitBlockFace).getLocation().add(0.5, 0.0, 0.5);
        player.damage(player.getLocation().distance(teleportBlockLocation) / 8.0, DamageSource.builder(DamageType.ENDER_PEARL).build());
        player.teleport(teleportBlockLocation, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0F, 1.0F);
    }
}
