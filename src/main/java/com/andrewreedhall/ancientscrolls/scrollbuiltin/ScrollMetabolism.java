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
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollMetabolism extends Scroll {
    public ScrollMetabolism() {
        super(
                NamespacedKey.fromString("metabolism", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Metabolism",

                "Slowly regenerates health when player has no food saturation"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.END_CITY),
                new ScrollGeneratorTreasure(0.2, Chest.class, Structure.JUNGLE_PYRAMID)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        if (player.getSaturation() > 0.0F) {
            return;
        }
        AttributeInstance attributeInstance = player.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance == null) {
            return;
        }
        double increasedHealth = player.getHealth() + 0.0065;
        if (increasedHealth > attributeInstance.getValue()) {
            return;
        }
        player.setHealth(increasedHealth);
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}
}
