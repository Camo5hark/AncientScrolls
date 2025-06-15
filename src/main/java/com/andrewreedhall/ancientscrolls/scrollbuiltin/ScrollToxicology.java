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
import com.andrewreedhall.ancientscrolls.scroll.ScrollEffectNegation;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffectType;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollToxicology extends ScrollEffectNegation {
    public ScrollToxicology() {
        super(
                NamespacedKey.fromString("toxicology", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Toxicology",
                PotionEffectType.POISON,
                "poison"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.05, EntityType.BOGGED),
                new ScrollGeneratorMonsterDrop(0.05, EntityType.WITCH),
                new ScrollGeneratorMonsterDrop(0.05, EntityType.CAVE_SPIDER),
                new ScrollGeneratorTreasure(0.2, Chest.class, Structure.JUNGLE_PYRAMID)
        };
    }
}
