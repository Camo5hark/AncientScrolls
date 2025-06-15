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
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollSpelunking extends Scroll {
    public ScrollSpelunking() {
        super(
                NamespacedKey.fromString("spelunking", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Spelunking",

                "Grants night vision when deep underground"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.ANCIENT_CITY),
                new ScrollGeneratorTreasure(0.2, StorageMinecart.class, Structure.MINESHAFT)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        PotionEffect nightVisionEffect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
        if (player.getLocation().getBlockY() > 30 || (nightVisionEffect != null && nightVisionEffect.getDuration() > 220)) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0, false, false));
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}
}
