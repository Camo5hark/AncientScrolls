package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollArchaicWorkoutRoutine extends ItemScrollNative implements Listener {
    public ScrollArchaicWorkoutRoutine() {
        super("archaic_workout_routine", "Archaic Workout Routine", new String[] {
                "Negates slowness"
        });
        this.putMCLootTableGenProb("entities/stray", 0.01);
        this.putMCLootTableGenProb("entities/witch", 0.05);
        this.vaultGenProb = 0.083;
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.SLOWNESS);
    }
}
