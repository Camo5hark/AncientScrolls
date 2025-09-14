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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollMicrobiology extends ItemScrollNative implements Listener {
    public ScrollMicrobiology() {
        super("microbiology", "Microbiology", new String[] {
                "Negates nausea"
        });
        this.putMCLootTableGenProb("chests/shipwreck_supply", 0.421);
        this.putMCLootTableGenProb("chests/shipwreck_map", 0.6);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.NAUSEA);
    }
}
