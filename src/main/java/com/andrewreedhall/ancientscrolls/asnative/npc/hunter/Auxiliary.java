package com.andrewreedhall.ancientscrolls.asnative.npc.hunter;

import com.andrewreedhall.ancientscrolls.npc.NPCInstance;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Function;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

final class Auxiliary implements Function<NPCInstance, Packet<?>[]> {
    Auxiliary() {}

    @Override
    public Packet<?>[] apply(NPCInstance npcInstance) {
        return new Packet[] {
                new ClientboundSetEquipmentPacket(
                        npcInstance.player.getId(),
                        List.of(
                                new Pair<>(EquipmentSlot.MAINHAND, new ItemStack(
                                        plugin().getUniversalRandom().nextBoolean() ? Items.IRON_SWORD : Items.BOW
                                ))
                        )
                )
        };
    }
}
