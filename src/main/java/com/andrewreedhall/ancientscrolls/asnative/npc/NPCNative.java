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

package com.andrewreedhall.ancientscrolls.asnative.npc;

import com.andrewreedhall.ancientscrolls.item.scroll.ItemScroll;
import com.andrewreedhall.ancientscrolls.npc.AncientScrollsNPC;
import com.andrewreedhall.ancientscrolls.npc.NPCInstance;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import oshi.util.tuples.Pair;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class NPCNative extends AncientScrollsNPC {
    public NPCNative(
            final String id,
            final String name,
            final NPCInstance.Skin skin,
            final Function<NPCInstance, Packet<?>[]> auxiliary,
            final Pair<Predicate<LivingEntity>, Double> generator,
            final Set<ItemScroll> resultScrolls,
            final Set<Pair<Material, Integer>> ingredientItemStackDescriptors
    ) {
        super(fromAncientScrollsNamespace(id), name, skin, auxiliary, generator, resultScrolls, ingredientItemStackDescriptors);
    }
}
