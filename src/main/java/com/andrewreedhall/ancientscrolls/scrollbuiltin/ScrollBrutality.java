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

import com.andrewreedhall.ancientscrolls.Utils;
import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.Vector;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollBrutality extends Scroll {
    public ScrollBrutality() {
        super(
                NamespacedKey.fromString("brutality", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Brutality",

                "Increases attack knockback",
                "Critical attacks launch target into the air"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.ANCIENT_CITY),
                new ScrollGeneratorTreasure(0.1, Chest.class, Structure.DESERT_PYRAMID),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.HOGLIN)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        Entity attacker = Utils.getActualAttacker(event);
        if (!(attacker instanceof Player player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        Vector velocity = player.getLocation().getDirection().multiply(1.2);
        double yVelocity;
        if (Utils.isPlayerOnGround(player) || player.getFallDistance() <= 0.0F) {
            yVelocity = 0.2;
        } else {
            yVelocity = 0.6;
            World world = player.getWorld();
            world.playSound(entity, Sound.BLOCK_ANVIL_PLACE, 0.3F, 0.3F);
        }
        entity.teleport(entity.getLocation().add(0.0, 0.1, 0.0));
        plugin().getServer().getScheduler().scheduleSyncDelayedTask(plugin(), () -> entity.setVelocity(velocity.setY(yVelocity)), 1L);
    }
}
