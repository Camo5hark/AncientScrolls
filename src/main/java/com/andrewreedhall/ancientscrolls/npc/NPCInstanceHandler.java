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

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.Set;

public final class NPCInstanceHandler implements Runnable, Listener {
    public final Set<NPCInstance> activeNPCInstances = new HashSet<>();

    public NPCInstanceHandler() {}

    @Override
    public void run() {
        this.activeNPCInstances.forEach(NPCInstance::tick);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player joiningPlayer = event.getPlayer();
        this.activeNPCInstances.forEach((final NPCInstance npcInstance) ->
                npcInstance.addToPlayersClient(((CraftPlayer) joiningPlayer).getHandle())
        );
    }
}
