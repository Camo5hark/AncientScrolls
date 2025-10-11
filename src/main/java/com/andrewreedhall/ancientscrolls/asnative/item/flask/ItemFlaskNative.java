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

package com.andrewreedhall.ancientscrolls.asnative.item.flask;

import com.andrewreedhall.ancientscrolls.item.flask.ItemFlask;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public abstract class ItemFlaskNative extends ItemFlask {
    public ItemFlaskNative(
            final String id,
            final String displayName,
            final NamedTextColor displayNameColor,
            final String[] lore,
            final Color color,
            final PotionEffectType buffPotionEffectType,
            final PotionEffectType debuffPotionEffectType
    ) {
        super(fromAncientScrollsNamespace(id), displayName, displayNameColor, lore, color, buffPotionEffectType, debuffPotionEffectType);
    }
}
