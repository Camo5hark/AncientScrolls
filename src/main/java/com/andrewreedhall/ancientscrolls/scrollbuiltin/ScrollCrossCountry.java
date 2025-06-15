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
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollCrossCountry extends Scroll {
    private static final Collection<Biome> HIGHLAND_BIOMES = Set.of(
            Biome.JAGGED_PEAKS,
            Biome.FROZEN_PEAKS,
            Biome.STONY_PEAKS,
            Biome.MEADOW,
            Biome.CHERRY_GROVE,
            Biome.GROVE,
            Biome.SNOWY_SLOPES,
            Biome.WINDSWEPT_HILLS,
            Biome.WINDSWEPT_GRAVELLY_HILLS,
            Biome.WINDSWEPT_FOREST
    );

    private final NamespacedKey attributeModifierIDMovementSpeed;

    public ScrollCrossCountry() {
        super(
                NamespacedKey.fromString("crosscountry", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER | Scroll.FLAG_SCHEDULE_PER_UNAFFECTED_PLAYER,
                "Cross Country",

                "Increases movement speed by 25%",
                "Grants jump boost in highlands"
        );
        this.attributeModifierIDMovementSpeed = this.createAttributeModifierID("movement_speed");
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.5, Chest.class, Structure.VILLAGE_TAIGA),
                new ScrollGeneratorTreasure(0.5, Chest.class, Structure.VILLAGE_SNOWY),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.STRAY)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        this.addAttributeModifierIfNotPresent(player, Attribute.MOVEMENT_SPEED, this.attributeModifierIDMovementSpeed, 0.25, AttributeModifier.Operation.ADD_SCALAR);
        if (HIGHLAND_BIOMES.contains(player.getLocation().getBlock().getBiome())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20, 1, false, false));
        }
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {
        this.removeAttributeModifierIfPresent(player, Attribute.MOVEMENT_SPEED, this.attributeModifierIDMovementSpeed);
    }
}
