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

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class NPCHandler implements Runnable, Listener {
    public final Set<NPCInstance> activeNPCInstances = new HashSet<>();
    private final Queue<Triplet<AncientScrollsNPC, UUID, Triplet<Double, Double, Double>>> generationQueue = new ConcurrentLinkedQueue<>();

    public NPCHandler() {}

    @Override
    public void run() {
        this.activeNPCInstances.forEach(NPCInstance::tick);
        final Set<Player> onlinePlayers = plugin()
                .getServer()
                .getOnlinePlayers()
                .stream()
                .filter((final Player onlinePlayer) -> !NPCInstance.is(((CraftEntity) onlinePlayer).getHandle()))
                .collect(Collectors.toUnmodifiableSet());
        plugin().getNPCRegistry().getAll().forEach((final AncientScrollsNPC npc) ->
                npc.generators.forEach((final Pair<Predicate<Player>, Double> generator) -> onlinePlayers
                        .stream()
                        .filter(generator.getA())
                        .forEach((final Player onlinePlayer) -> {
                            if (plugin().getUniversalRandom().nextDouble() > generator.getB()) {
                                return;
                            }
                            // TODO create thread to search for a place to spawn instance
                        })
                ));
        Triplet<AncientScrollsNPC, UUID, Triplet<Double, Double, Double>> generation;
        while ((generation = this.generationQueue.poll()) != null) {
            generation.getA().createInstance(
                    plugin().getServer().getWorld(generation.getB()),
                    new Location(
                            null,
                            generation.getC().getA(),
                            generation.getC().getB(),
                            generation.getC().getC()
                    )
            );
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player joiningPlayer = event.getPlayer();
        this.activeNPCInstances.forEach((final NPCInstance npcInstance) ->
                npcInstance.addToPlayersClient(((CraftPlayer) joiningPlayer).getHandle())
        );
    }
}
