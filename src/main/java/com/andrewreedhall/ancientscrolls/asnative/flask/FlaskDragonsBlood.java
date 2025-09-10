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

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskDragonsBlood extends ItemFlaskNative{
    public FlaskDragonsBlood() {
        super("dragons_blood", GOLD + "Dragon's Blood", new String[] {
                "Ancient dragons were born in fire"
        }, Color.RED, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.BLINDNESS);
        this.putMCLootTableGenProb("chests/ruined_portal", 0.205);
        this.putMCLootTableGenProb("chests/nether_bridge", 0.179);
        this.putMCLootTableGenProb("chests/bastion_other", 0.244);
    }
}
