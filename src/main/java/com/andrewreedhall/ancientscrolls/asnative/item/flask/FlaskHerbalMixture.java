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

package com.andrewreedhall.ancientscrolls.asnative.item.flask;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public final class FlaskHerbalMixture extends ItemFlaskNative {
    public FlaskHerbalMixture() {
        super("herbal_mixture", "Herbal Mixture", NamedTextColor.GREEN, new String[] {
                "Composed of spring flowers and insects"
        }, Color.GREEN, PotionEffectType.JUMP_BOOST, PotionEffectType.INFESTED);
        this.putMCLootTableGenProb("chests/village/village_plains_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_savanna_house", 0.266);
        this.putMCLootTableGenProb("chests/jungle_temple", 0.333);
    }
}
