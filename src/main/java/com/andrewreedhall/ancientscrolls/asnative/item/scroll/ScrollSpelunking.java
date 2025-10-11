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

import org.bukkit.entity.Player;

public final class ScrollSpelunking extends ItemScrollNative {
    public ScrollSpelunking() {
        super("spelunking", "Spelunking", new String[] {
                "Night vision at Y=30 and below"
        });
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.271);
        this.putMCLootTableGenProb("archaeology/trail_ruins_rare", 0.083);
        this.putMCLootTableGenProb("chests/ancient_city", 0.161);
        this.addPotionEffectToEquippingPlayers(NIGHT_VISION_POTION_EFFECT, (final Player equippingPlayer) -> equippingPlayer.getLocation().getBlockY() <= 30);
    }
}
