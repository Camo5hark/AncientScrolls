package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollArchaicEyeChart extends ItemScrollNative implements Listener {
    public ScrollArchaicEyeChart() {
        super("archaic_eye_chart", "Archaic Eye Chart", new String[] {
                "Negates blindness"
        });
        this.putMCLootTableGenProb("chests/shipwreck_supply", 0.421);
        this.putMCLootTableGenProb("chests/shipwreck_map", 0.6);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.BLINDNESS);
    }
}
