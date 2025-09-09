package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollImmunityResearch extends ItemScrollNative implements Listener {
    public ScrollImmunityResearch() {
        super("immunity_research", "Immunity Research", new String[] {
                "Reduces bad effect duration by 25%"
        });
        this.putMCLootTableGenProb("entities/witch", 0.05);
        this.putMCLootTableGenProb("chests/ancient_city", 0.161);
        this.ominousVaultGenProb = 0.139;
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player effectedPlayer)) {
            return;
        }
        final PotionEffect newPotionEffect = event.getNewEffect();
        if (newPotionEffect == null || !CommonSets.BAD_POTION_EFFECT_TYPES.contains(newPotionEffect.getType()) || !this.isEquipping(effectedPlayer)) {
            return;
        }
        final int newPotionEffectDuration = newPotionEffect.getDuration();
        plugin().scheduleTask(
                (final BukkitScheduler scheduler) ->
                        scheduler.scheduleSyncDelayedTask(
                                plugin(),
                                () -> {
                                    effectedPlayer.removePotionEffect(newPotionEffect.getType());
                                    effectedPlayer.playSound(effectedPlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                                    effectedPlayer.getWorld().spawnParticle(
                                            Particle.HAPPY_VILLAGER,
                                            effectedPlayer.getEyeLocation(),
                                            5,
                                            0.5,
                                            0.5,
                                            0.5
                                    );
                                },
                                newPotionEffectDuration - (newPotionEffectDuration / 4)
                        )
        );
    }
}
