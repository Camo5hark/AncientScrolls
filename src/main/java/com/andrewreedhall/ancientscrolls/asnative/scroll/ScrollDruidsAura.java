package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public final class ScrollDruidsAura extends ItemScrollNative {
    public ScrollDruidsAura() {
        super("druids_aura", "Druid's Aura", new String[] {
                "Slowly damages monsters within a 3 block radius"
        });
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final World equippingPlayerWorld = equippingPlayer.getWorld();
            final Location equippingPlayerLocation = equippingPlayer.getLocation();
            equippingPlayerWorld
                    .getNearbyEntitiesByType(Monster.class, equippingPlayer.getLocation(), 3.0)
                    .stream()
                    .filter((final Monster nearbyMonster) -> !(nearbyMonster instanceof Boss) && nearbyMonster.getLocation().distanceSquared(equippingPlayerLocation) <= 9.0)
                    .forEach((final Monster nearbyMonster) -> {
                        nearbyMonster.damage(1.0);
                        equippingPlayerWorld.spawnParticle(Particle.HAPPY_VILLAGER, equippingPlayer.getEyeLocation(), 10, 3.0, 3.0, 3.0);
                    });
        }, 20L);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.5);
    }
}
