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

package com.andrewreedhall.ancientscrolls.npc;

import com.andrewreedhall.ancientscrolls.util.Randomizer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class NPCHandler implements Runnable, Listener {
    private static final Randomizer<AncientScrollsNPC> NPC_RANDOMIZER = new Randomizer<>();

    public final Set<NPCInstance> activeNPCInstances = new HashSet<>();

    public NPCHandler() {}

    @Override
    public void run() {
        this.activeNPCInstances.forEach(NPCInstance::tick);
        this.activeNPCInstances.removeIf(NPCInstance::isTTLUp);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player joiningPlayer = event.getPlayer();
        this.activeNPCInstances.forEach((final NPCInstance npcInstance) ->
                npcInstance.addToClient(((CraftPlayer) joiningPlayer).getHandle())
        );
    }

    @EventHandler
    public void onEntitySpawn(final EntitySpawnEvent event) {
        if (!plugin().npc_generation_enabled ||
                !(event.getEntity() instanceof LivingEntity spawnedLivingEntity) ||
                !event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)
        ) {
            return;
        }
        final List<AncientScrollsNPC> registeredNPCs = new ArrayList<>(
                plugin()
                        .getNPCRegistry()
                        .getAll()
                        .stream()
                        .filter((final AncientScrollsNPC npc) -> npc.generation_enabled)
                        .toList()
        );
        NPC_RANDOMIZER.sort(registeredNPCs, null);
        for (final AncientScrollsNPC registeredNPC : registeredNPCs) {
            if (registeredNPC.generate(spawnedLivingEntity)) {
                spawnedLivingEntity.remove();
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
        if (!event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }
        final NPCInstance interactedNPCInstance = AncientScrollsNPC.getInstance(event.getRightClicked());
        if (interactedNPCInstance == null) {
            return;
        }
        final Player interactingPlayer = event.getPlayer();
        interactingPlayer.openInventory(interactedNPCInstance.merchantInventoryViewBuilder.build(interactingPlayer));
    }
}
