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

public final class FlaskUnstableConcoction extends ItemFlaskNative {
    public FlaskUnstableConcoction() {
        super("unstable_concoction", "Unstable Concoction", NamedTextColor.AQUA, new String[] {
                "Boost of energy at a cost"
        }, Color.FUCHSIA, PotionEffectType.SPEED, PotionEffectType.WEAKNESS);
        this.putMCLootTableGenProb("chests/stronghold_corridor", 0.329);
        this.putMCLootTableGenProb("chests/stronghold_crossing", 0.343);
        this.putMCLootTableGenProb("chests/trial_chambers/entrance", 0.551);
        this.putMCLootTableGenProb("chests/trial_chambers/intersection", 0.215);
        this.putMCLootTableGenProb("chests/trial_chambers/supply", 0.204);
        this.putMCLootTableGenProb("chests/nether_bridge", 0.179);
        this.putMCLootTableGenProb("chests/bastion_other", 0.19);
    }
}
