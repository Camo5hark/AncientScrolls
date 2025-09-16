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

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollJousting extends ItemScrollNative {
    public ScrollJousting() {
        super("jousting", "Jousting", new String[] {
                "Double melee damage and knockback in vehicles"
        });
        this.putMCLootTableGenProb("chests/bastion_hoglin_stable", 0.228);
        this.putMCLootTableGenProb("entities/hoglin", 0.01);
        this.putMCLootTableGenProb("entities/ravager", 0.01);
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.ATTACK_DAMAGE,
                                new AttributeModifier(
                                        this.createSubkey("attack_damage"),
                                        1.0,
                                        AttributeModifier.Operation.ADD_SCALAR
                                )
                        ),
                        new Pair<>(
                                Attribute.ATTACK_KNOCKBACK,
                                new AttributeModifier(
                                        this.createSubkey("attack_knockback"),
                                        1.0,
                                        AttributeModifier.Operation.ADD_SCALAR
                                )
                        )
                ),
                (final Player equippingPlayer) -> equippingPlayer.getVehicle() != null,
                null
        );
    }
}
