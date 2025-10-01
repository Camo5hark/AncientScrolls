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
import com.google.common.base.CaseFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Optional;

public final class ScrollVigilance extends ItemScrollNative {
    public ScrollVigilance() {
        super("vigilance", "Vigilance", new String[] {
                "Notifies user of the most dangerous monster within 30 blocks"
        });
        // TODO dungeon
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.423);
        this.vaultGenProb = 0.24;
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final Location equippingPlayerLocation = equippingPlayer.getLocation();
            final Optional<Monster> highestDamagingNearbyMonsterOptional = equippingPlayer
                    .getWorld()
                    .getNearbyEntitiesByType(Monster.class, equippingPlayerLocation, 30.0)
                    .stream()
                    .max(
                            Comparator.comparingDouble(
                                    (Monster nearbyMonster) ->
                                            BukkitUtil.getAttributeInstance(nearbyMonster, Attribute.ATTACK_DAMAGE).getValue()
                            )
                    );
            if (highestDamagingNearbyMonsterOptional.isEmpty()) {
                return;
            }
            final Monster highestDamagingNearbyMonster = highestDamagingNearbyMonsterOptional.get();
            equippingPlayer.sendActionBar(Component.join(
                    JoinConfiguration.noSeparators(),
                    Component.text(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, highestDamagingNearbyMonster.getType().toString()), NamedTextColor.RED),
                    Component.text(" detected ", NamedTextColor.GOLD),
                    Component.text((int) Math.round(highestDamagingNearbyMonster.getLocation().distance(equippingPlayerLocation)), NamedTextColor.RED),
                    Component.text(" blocks away", NamedTextColor.GOLD)
            ));
        }, 60L);
    }
}
