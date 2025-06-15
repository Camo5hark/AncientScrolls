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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.generator.structure.Structure;

import java.util.Collection;
import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollHellsBlessing extends Scroll {
    private static final Collection<EntityType> NETHER_MONSTER_TYPES = Set.of(
            EntityType.BLAZE,
            EntityType.GHAST,
            EntityType.HOGLIN,
            EntityType.MAGMA_CUBE,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIFIED_PIGLIN
    );

    public ScrollHellsBlessing() {
        super(
                NamespacedKey.fromString("hellsblessing", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Hell's Blessing",

                "Reduces damage from Nether monsters by 15%"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.FORTRESS),
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.BASTION_REMNANT),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.HOGLIN),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.WITHER_SKELETON),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.BLAZE),
                new ScrollGeneratorMonsterDrop(0.05, EntityType.PIGLIN_BRUTE)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        this.addDamageToPlayerByScalar(event, -0.15, NETHER_MONSTER_TYPES);
    }
}
