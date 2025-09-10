package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollMicrobiology extends ItemScrollNative implements Listener {
    public ScrollMicrobiology() {
        super("microbiology", "microbiology", new String[] {
                "Negates nausea"
        });
        this.putMCLootTableGenProb("chests/shipwreck_supply", 0.421);
        this.putMCLootTableGenProb("chests/shipwreck_map", 0.6);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.NAUSEA);
    }
}
