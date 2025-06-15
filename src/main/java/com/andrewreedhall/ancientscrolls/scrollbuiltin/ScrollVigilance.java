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
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.generator.structure.Structure;

import java.util.Optional;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollVigilance extends Scroll {
    public ScrollVigilance() {
        super(
                NamespacedKey.fromString("vigilance", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Vigilance",

                "Notifies user of the most dangerous monster within 24 blocks"
        );
        this.scheduledTickPeriod = 100;
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.3, StorageMinecart.class, Structure.MINESHAFT)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        double radius = Math.min(24, player.getWorld().getSimulationDistance() * 16);
        Optional<Entity> highestDamagingNearbyMonster = player.getNearbyEntities(radius, radius, radius).stream().filter((Entity nearbyEntity) -> nearbyEntity instanceof Monster).max((entity0, entity1) -> {
            double monster0Damage = getMonsterDamage((Monster) entity0);
            double monster1Damage = getMonsterDamage((Monster) entity1);
            return Double.compare(monster0Damage, monster1Damage);
        });
        if (highestDamagingNearbyMonster.isEmpty()) {
            return;
        }
        int distance = (int) Math.round(player.getLocation().distance(highestDamagingNearbyMonster.get().getLocation()));
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(ChatColor.GOLD + highestDamagingNearbyMonster.get().getName() + ChatColor.RED + " detected " + distance + " blocks away")
        );
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    private static double getMonsterDamage(Monster monster) {
        AttributeInstance attributeInstance = monster.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attributeInstance == null) {
            return 0.0;
        }
        return attributeInstance.getValue();
    }
}
