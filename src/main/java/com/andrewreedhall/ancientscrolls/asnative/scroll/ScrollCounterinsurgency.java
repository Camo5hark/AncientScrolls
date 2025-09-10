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

public final class ScrollCounterinsurgency extends ItemScrollNative implements Listener {
    public ScrollCounterinsurgency() {
        super("counterinsurgency", "Counterinsurgency", new String[] {
                "+15% damage towards and -15% damage from illagers"
        });
        this.putMCLootTableGenProb("chests/pillager_outpost", 0.25);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.283);
        this.putMCLootTableGenProb("chests/village/village_armorer", 0.1);
        this.putMCLootTableGenProb("chests/village/village_weaponsmith", 0.056);
        this.putMCLootTableGenProb("entities/evoker", 0.01);
        this.putMCLootTableGenProb("entities/pillager", 0.005);
        this.putMCLootTableGenProb("entities/vindicator", 0.005);
        this.putMCLootTableGenProb("entities/vex", 0.01);
        this.putMCLootTableGenProb("entities/witch", 0.005);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.ILLAGERS, 0.15);
    }
}
