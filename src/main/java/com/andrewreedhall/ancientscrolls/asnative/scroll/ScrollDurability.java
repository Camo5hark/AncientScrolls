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
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollDurability extends ItemScrollNative {
    public ScrollDurability() {
        super("durability", "Durability", new String[] {
                "+8 max health"
        });
        this.putMCLootTableGenProb("chests/stronghold_corridor", 0.025);
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.21);
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.MAX_HEALTH,
                                new AttributeModifier(
                                        createSubkey("max_health"),
                                        8.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                )
                        )
                ),
                CONDITION_BYPASS,
                null
        );
    }
}
