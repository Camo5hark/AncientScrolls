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

public final class ScrollLongevity extends ItemScrollNative {
    public ScrollLongevity() {
        super("longevity", "Longevity", new String[] {
                "+4 max health"
        });
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.MAX_HEALTH,
                                new AttributeModifier(
                                        this.createSubkey("max_health"),
                                        4.0,
                                    AttributeModifier.Operation.ADD_NUMBER
                                )
                        )
                ),
                CONDITION_BYPASS,
                null
        );
        this.putMCLootTableGenProb("chests/stronghold_corridor", 0.119);
        this.putMCLootTableGenProb("chests/stronghold_library", 0.678);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.135);
        this.putMCLootTableGenProb("chests/bastion_bridge", 0.112);
        this.ominousVaultGenProb = 0.015;
    }
}
