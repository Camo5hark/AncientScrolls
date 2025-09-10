package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollAutotrophy extends ItemScrollNative implements Listener {
    public ScrollAutotrophy() {
        super("autotrophy", "Autotrophy", new String[] {
                "Negates hunger effect"
        });
        this.putMCLootTableGenProb("entities/husk", 0.01);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.HUNGER);
    }
}
