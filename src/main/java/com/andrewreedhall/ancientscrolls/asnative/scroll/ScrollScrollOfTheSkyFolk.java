package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class ScrollScrollOfTheSkyFolk extends ItemScrollNative implements Listener {
    public ScrollScrollOfTheSkyFolk() {
        super("scroll_of_the_sky_folk", "Scroll of the Sky Folk", new String[] {
                "Negates fall damage"
        });
        this.special = true;
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL) ||
                !(event.getEntity() instanceof Player damagedPlayer) ||
                !this.isEquipping(damagedPlayer)
        ) {
            return;
        }
        event.setCancelled(true);
    }
}
