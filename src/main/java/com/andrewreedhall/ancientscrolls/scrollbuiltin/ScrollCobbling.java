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
import com.andrewreedhall.ancientscrolls.scroll.ScrollAttributeModifer;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.generator.structure.Structure;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollCobbling extends ScrollAttributeModifer {
    public ScrollCobbling() {
        super(
                NamespacedKey.fromString("cobbling", plugin()),
                "Cobbling",
                Attribute.MOVEMENT_SPEED,
                0.15,
                AttributeModifier.Operation.ADD_SCALAR,

                "Increases user movement speed by 15%"
        );
        this.flags |= Scroll.FLAG_REGISTER_EVENTS;
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.VILLAGE_PLAINS),
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.VILLAGE_DESERT),
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.VILLAGE_SAVANNA),
                new ScrollGeneratorTreasure(0.2, Chest.class, Structure.RUINED_PORTAL)
        };
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!Utils.isPlayerOnGround(player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 1, 0.2, 0.05, 0.2, 0.05);
    }
}
