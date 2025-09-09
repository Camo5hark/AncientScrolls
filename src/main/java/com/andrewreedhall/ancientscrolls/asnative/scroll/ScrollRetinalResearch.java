package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollRetinalResearch extends ItemScrollNative implements Listener {
    public ScrollRetinalResearch() {
        super("retinal_research", "Retinal Research", new String[] {
                "Negates darkness"
        });
        this.putMCLootTableGenProb("chests/ancient_city", 0.084);
        this.putMCLootTableGenProb("chests/ancient_city_ice_box", 0.549);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.DARKNESS);
    }
}
