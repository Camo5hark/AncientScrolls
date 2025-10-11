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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollRetinalResearch extends ItemScrollNative implements Listener {
    public ScrollRetinalResearch() {
        super("retinal_research", "Retinal Research", new String[] {
                "Negates darkness"
        });
        this.putMCLootTableGenProb("chests/ancient_city", 0.084);
        this.putMCLootTableGenProb("chests/ancient_city_ice_box", 0.549);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.DARKNESS);
    }
}
