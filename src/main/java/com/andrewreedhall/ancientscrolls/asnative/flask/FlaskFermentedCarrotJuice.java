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

package com.andrewreedhall.ancientscrolls.asnative.flask;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public final class FlaskFermentedCarrotJuice extends ItemFlaskNative {
    public FlaskFermentedCarrotJuice() {
        super("fermented_carrot_juice", "Fermented Carrot Juice", NamedTextColor.GREEN, new String[] {
                "Carrots are good for your eyesight"
        }, Color.ORANGE, PotionEffectType.NIGHT_VISION, PotionEffectType.NAUSEA);
        this.putMCLootTableGenProb("chests/village/village_desert_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_plains_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_savanna_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_snowy_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_taiga_house", 0.266);
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.423);
    }
}
