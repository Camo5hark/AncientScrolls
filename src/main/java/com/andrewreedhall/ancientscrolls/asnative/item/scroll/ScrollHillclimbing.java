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

package com.andrewreedhall.ancientscrolls.asnative.item.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ScrollHillclimbing extends ItemScrollNative {
    public ScrollHillclimbing() {
        super("hillclimbing", "Hillclimbing", new String[] {
                "Jump boost in highland biomes"
        });
        this.putMCLootTableGenProb("chests/ruined_portal", 0.205);
        this.putMCLootTableGenProb("entities/spider", 0.005);
        this.putMCLootTableGenProb("entities/cave_spider", 0.01);
        this.addPotionEffectToEquippingPlayers(
                new PotionEffect(PotionEffectType.JUMP_BOOST, 25, 2, false),
                (final Player equippingPlayer) -> CommonSets.HIGHLAND_BIOMES.contains(equippingPlayer.getLocation().getBlock().getBiome())
        );
    }
}
