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

package com.andrewreedhall.ancientscrolls.asnative.npc.prospectre;

import com.andrewreedhall.ancientscrolls.npc.NPCInstance;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Function;

final class Auxiliary implements Function<NPCInstance, Packet<?>[]> {
    Auxiliary() {}

    @Override
    public Packet<?>[] apply(final NPCInstance npcInstance) {
        return new Packet[] {
                new ClientboundSetEquipmentPacket(
                        npcInstance.player.getId(),
                        List.of(new Pair<>(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE)))
                )
        };
    }
}
