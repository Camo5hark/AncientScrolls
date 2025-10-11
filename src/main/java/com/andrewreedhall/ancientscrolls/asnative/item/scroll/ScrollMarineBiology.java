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

import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class ScrollMarineBiology extends ItemScrollNative {
    public ScrollMarineBiology() {
        super("marine_biology", "Marine Biology", new String[] {
                "Breathe underwater on a full stomach"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/elder_guardian", 1.0);
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            if (equippingPlayer.getFoodLevel() < 20 || !equippingPlayer.getEyeLocation().getBlock().getType().equals(Material.WATER)) {
                return;
            }
            equippingPlayer.setRemainingAir(equippingPlayer.getMaximumAir());
        }, 1L);
    }
}
