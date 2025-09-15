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

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollCobbling extends ItemScrollNative {
    public ScrollCobbling() {
        super("cobbling", "Cobbling", new String[] {
                "+15% movement speed"
        });
        // TODO dungeon
        this.putMCLootTableGenProb("chests/village/village_cartographer", 0.333);
        this.putMCLootTableGenProb("chests/village/village_temple", 0.448);
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.MOVEMENT_SPEED,
                                new AttributeModifier(
                                        this.createSubkey("movement_speed"),
                                        0.15,
                                        AttributeModifier.Operation.ADD_SCALAR
                                )
                        )
                ),
                null,
                (final Player player) -> {
                    if (player.isSneaking() || player.getForwardsMovement() <= 0.0F || !BukkitUtil.isPlayerOnGround(player)) {
                        return;
                    }
                    player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0.0, 0.1, 0.0), 3, 0.25, 0.1, 0.25, 0.2);
                }
        );
    }
}
