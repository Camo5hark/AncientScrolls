package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public final class ScrollNocturnal extends ItemScrollNative implements Listener {
    public ScrollNocturnal() {
        super("nocturnal", "Nocturnal", new String[] {
                "Prevents phantoms from spawning nearby"
        });
        this.putMCLootTableGenProb("entities/phantom", 0.01);
        this.vaultGenProb = 0.104;
    }

    @EventHandler
    public void onEntitySpawn(final EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Phantom spawnedPhantom)) {
            return;
        }
        final boolean nearbyPlayerIsEquipping = spawnedPhantom
                .getWorld()
                .getNearbyEntitiesByType(Player.class, spawnedPhantom.getLocation(), 10.0, 34.0, 10.0)
                .stream()
                .anyMatch(this::isEquipping);
        if (!nearbyPlayerIsEquipping) {
            return;
        }
        event.setCancelled(true);
    }
}
