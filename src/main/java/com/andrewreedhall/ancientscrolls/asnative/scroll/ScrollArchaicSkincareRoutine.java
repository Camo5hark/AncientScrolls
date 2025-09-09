package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollArchaicSkincareRoutine extends ItemScrollNative implements Listener {
    public ScrollArchaicSkincareRoutine() {
        super("archaic_skincare_routine", "Archaic Skincare Routine", new String[] {
                "Negates wither effect"
        });
        this.putMCLootTableGenProb("entities/wither", 1.0);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.WITHER);
    }
}
