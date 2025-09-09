package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollEndurance extends ItemScrollNative implements Listener {
    public ScrollEndurance() {
        super("endurance", "Endurance", new String[] {
                "Negates mining fatigue"
        });
        this.putMCLootTableGenProb("entities/elder_guardian", 1.0);
        this.putMCLootTableGenProb("entities/guardian", 0.005);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.MINING_FATIGUE);
    }
}
