package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollAdvancedPhysics extends ItemScrollNative implements Listener {
    public ScrollAdvancedPhysics() {
        super("advanced_physics", "Advanced Physics", new String[] {
                "Negates levitation"
        });
        this.putMCLootTableGenProb("entities/shulker", 0.01);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.LEVITATION);
    }
}
