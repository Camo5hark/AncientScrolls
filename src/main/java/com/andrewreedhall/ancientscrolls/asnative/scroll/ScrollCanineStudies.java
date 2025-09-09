package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ScrollCanineStudies extends ItemScrollNative implements Listener {
    private final NamespacedKey pdkApplyNightVisionEffect;

    public ScrollCanineStudies() {
        super("canine_studies", "Canine Studies", new String[] {
                "Night vision every other night",
                "+20% damage to skeletons"
        });
        this.pdkApplyNightVisionEffect = this.createSubkey("apply_night_vision_effect");
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("entities/wither_skeleton", 0.01);
        this.addNightVisionEffectToEquippingPlayers((final Player equippingPlayer) -> {
            final PersistentDataContainer equippingPlayerPDC = equippingPlayer.getPersistentDataContainer();
            final boolean condition = !equippingPlayer.getWorld().isDayTime() &&
                    equippingPlayerPDC.getOrDefault(this.pdkApplyNightVisionEffect, PersistentDataType.BOOLEAN, false);
            if (condition) {
                equippingPlayerPDC.remove(this.pdkApplyNightVisionEffect);
            }
            return condition;
        });
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof AbstractSkeleton) || !(BukkitUtil.getDamager(event) instanceof Player playerDamager) || !this.isEquipping(playerDamager)) {
            return;
        }
        event.setDamage(event.getDamage() + (event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) * 0.2));
    }
}
