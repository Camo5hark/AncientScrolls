package com.andrewreedhall.ancientscrolls.npc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public interface NPCPlayerAccess {
    NPCInstance getNPCInstance();

    default Player getPlayer() {
        return (Player) plugin().getServer().getEntity(this.getNPCInstance().player.getUUID());
    }

    default void onTTLUp() {
        final Player npcInstancePlayer = this.getPlayer();
        final World npcInstancePlayerWorld = npcInstancePlayer.getWorld();
        npcInstancePlayerWorld.getNearbyPlayers(npcInstancePlayer.getLocation(), 10.0).forEach((final Player nearbyPlayer) -> {
            if (nearbyPlayer.getOpenInventory().getTopInventory().getHolder() != null) {
                return;
            }
            nearbyPlayer.closeInventory();
        });
        final Location npcInstancePlayerEyeLocation = npcInstancePlayer.getEyeLocation();
        npcInstancePlayerWorld.spawn(npcInstancePlayerEyeLocation, Vex.class);
        npcInstancePlayerWorld.playSound(npcInstancePlayer.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        npcInstancePlayerWorld.spawnParticle(
                Particle.SOUL_FIRE_FLAME,
                npcInstancePlayerEyeLocation,
                100,
                1.0,
                1.0,
                1.0
        );
    }
}
