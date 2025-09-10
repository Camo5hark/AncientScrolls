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

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollHellsBlessing extends ItemScrollNative implements Listener {
    public ScrollHellsBlessing() {
        super("hells_blessing", "Hell's Blessing", new String[] {
                "+15% damage towards and -15% damage from nether monsters"
        });
        this.putMCLootTableGenProb("entities/blaze", 0.005);
        this.putMCLootTableGenProb("entities/ghast", 0.005);
        this.putMCLootTableGenProb("entities/hoglin", 0.005);
        this.putMCLootTableGenProb("entities/magma_cube", 0.005);
        this.putMCLootTableGenProb("entities/piglin", 0.005);
        this.putMCLootTableGenProb("entities/piglin_brute", 0.005);
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/wither_skeleton", 0.005);
        this.putMCLootTableGenProb("entities/zoglin", 0.005);
        this.putMCLootTableGenProb("entities/zombified_piglin", 0.005);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.NETHER_MONSTERS, 0.15);
    }
}
