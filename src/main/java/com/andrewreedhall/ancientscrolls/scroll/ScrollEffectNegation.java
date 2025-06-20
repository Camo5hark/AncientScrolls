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

    License file: COPYING
    Email for contact: camo5hark10@gmail.com

 */

package com.andrewreedhall.ancientscrolls.scroll;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public abstract class ScrollEffectNegation extends Scroll {
    private final PotionEffectType effectType;

    /**
     * All parameters should be statically filled in inheriting class's super call<br>
     * Will cause error upon registration if this condition is not met
     *
     * @param id                  @see #id
     * @param flags               @see #flags
     * @param title               the human-readable name of this scroll for the item stack lore
     */
    public ScrollEffectNegation(NamespacedKey id, int flags, String title, PotionEffectType effectType, String effectTypeName) {
        super(id, flags, title, "Negates " + effectTypeName + " effect");
        this.effectType = effectType;
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        this.negateEffect(event, this.effectType);
    }
}
