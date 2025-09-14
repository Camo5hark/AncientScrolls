package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class ScrollAeronautics extends ItemScrollNative implements Listener {
    public ScrollAeronautics() {
        super("aeronautics", "Aeronautics", new String[] {
                "Reduces elytra crash damage by 75%"
        });
        this.special = true;
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.21);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || !damagedPlayer.isGliding()) {
            return;
        }
        event.setDamage(event.getDamage() - (event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) * 0.75));
        damagedPlayer.spawnParticle(Particle.HEART, damagedPlayer.getLocation(), 5, 1.0, 0.5, 1.0, 0.1);
    }
}
