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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollSnowStepping extends Scroll {
    private final NamespacedKey attributeModifierIDMovementSpeed;

    public ScrollSnowStepping() {
        super(
                NamespacedKey.fromString("snowstepping", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Snow Stepping",

                "Increases movement speed by 12% on snow",
                "Turns powdered snow into regular snow"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.05, EntityType.STRAY),
                new ScrollGeneratorTreasure(0.2, Chest.class, Structure.VILLAGE_SNOWY),
                new ScrollGeneratorTreasure(0.2, Chest.class, Structure.VILLAGE_TAIGA),
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.IGLOO)
        };
        this.attributeModifierIDMovementSpeed = this.createAttributeModifierID("movement_speed");
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        Location location = player.getLocation();
        Block blockAt = location.getBlock();
        Block blockBelow = location.subtract(0.0, 1.0, 0.0).getBlock();
        Material blockBelowType = blockBelow.getType();
        if (blockAt.getType().equals(Material.SNOW) || blockBelowType.equals(Material.SNOW_BLOCK)) {
            this.addAttributeModifierIfNotPresent(player, Attribute.MOVEMENT_SPEED, this.attributeModifierIDMovementSpeed, 0.15, AttributeModifier.Operation.ADD_SCALAR);
        } else {
            this.removeAttributeModifierIfPresent(player, Attribute.MOVEMENT_SPEED, this.attributeModifierIDMovementSpeed);
        }
        if (blockBelowType.equals(Material.POWDER_SNOW)) {
            blockBelow.setBlockData(Material.SNOW_BLOCK.createBlockData());
        }
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {
        this.removeAttributeModifierIfPresent(player, Attribute.MOVEMENT_SPEED, this.attributeModifierIDMovementSpeed);
    }
}
