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
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollNetherLife extends ItemScrollNative implements Listener {
    public ScrollNetherLife() {
        super("nether_life", "Nether Life", new String[] {
                "Night vision in the nether",
                "+20% damage towards and -20% damage from nether monsters"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/wither", 1.0);
        this.addPotionEffectToEquippingPlayers(NIGHT_VISION_POTION_EFFECT, (final Player equippingPlayer) -> equippingPlayer.getWorld().getEnvironment().equals(World.Environment.NETHER));
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.NETHER_MONSTERS, 0.2);
    }
}
