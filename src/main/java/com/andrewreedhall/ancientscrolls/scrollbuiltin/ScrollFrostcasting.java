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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.structure.Structure;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollFrostcasting extends Scroll {
    public ScrollFrostcasting() {
        super(
                NamespacedKey.fromString("frostcasting", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Frostcasting",

                "Thrown snowballs minimally damage targets",
                "Thrown snowballs create temporary ice barrier upon hitting ground"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.1, Chest.class, Structure.VILLAGE_TAIGA),
                new ScrollGeneratorTreasure(0.1, Chest.class, Structure.VILLAGE_SNOWY),
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.IGLOO)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball projectileSnowball)) {
            return;
        }
        if (!(projectileSnowball.getShooter() instanceof Player shooterPlayer)) {
            return;
        }
        if (!this.doesPlayerHaveScroll(shooterPlayer)) {
            return;
        }
        Entity hitEntity = event.getHitEntity();
        Block hitBlock = event.getHitBlock();
        BlockFace hitBlockFace = event.getHitBlockFace();
        if (hitEntity instanceof LivingEntity hitLivingEntity) {
            // hit entity
            hitLivingEntity.damage(1.0);
        } else if (hitBlock != null && hitBlockFace != null) {
            // hit block
            setAdjacentBlockType(hitBlock, hitBlockFace, Material.ICE);
            plugin().getServer().getScheduler().scheduleSyncDelayedTask(plugin(), () -> setAdjacentBlockType(hitBlock, hitBlockFace, Material.AIR), 60L);
        }
    }

    private static void setAdjacentBlockType(Block block, BlockFace direction, Material type) {
        block.getWorld().setBlockData(block.getLocation().add(direction.getDirection()), type.createBlockData());
    }
}
