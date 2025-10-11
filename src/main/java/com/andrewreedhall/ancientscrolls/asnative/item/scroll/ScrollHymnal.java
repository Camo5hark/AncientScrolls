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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollHymnal extends ItemScrollNative implements Listener {
    public ScrollHymnal() {
        super("hymnal", "Hymnal", new String[] {
                "+15% damage towards and -15% damage from undead monsters"
        });
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("entities/drowned", 0.005);
        this.putMCLootTableGenProb("entities/husk", 0.005);
        this.putMCLootTableGenProb("entities/phantom", 0.005);
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/skeleton_horse", 0.005);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.putMCLootTableGenProb("entities/wither_skeleton", 0.005);
        this.putMCLootTableGenProb("entities/zoglin", 0.005);
        this.putMCLootTableGenProb("entities/zombie", 0.005);
        this.putMCLootTableGenProb("entities/zombie_horse", 0.005);
        this.putMCLootTableGenProb("entities/zombie_villager", 0.005);
        this.putMCLootTableGenProb("entities/zombified_piglin", 0.005);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.UNDEAD_MONSTERS, 0.15);
    }
}
