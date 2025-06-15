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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.generator.structure.Structure;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollHymnal extends Scroll {
    public ScrollHymnal() {
        super(
                NamespacedKey.fromString("hymnal", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Hymnal",

                "Reduces damage from undead monsters by 15%"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, StorageMinecart.class, Structure.MINESHAFT),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.ZOMBIE),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.SKELETON),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.PHANTOM),
                new ScrollGeneratorTreasure(0.1, Chest.class, Structure.DESERT_PYRAMID)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        this.addDamageToPlayerByScalar(event, -0.15, Utils.ENTITY_TYPES_UNDEAD);
    }
}
