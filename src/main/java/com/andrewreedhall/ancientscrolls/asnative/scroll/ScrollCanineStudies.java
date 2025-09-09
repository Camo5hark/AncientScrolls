package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public final class ScrollCanineStudies extends ItemScrollNative implements Listener {
    public ScrollCanineStudies() {
        super("canine_studies", "Canine Studies", new String[] {
                "Night vision at night",
                "+20% damage to skeletons"
        });
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("entities/wither_skeleton", 0.01);
        this.addNightVisionEffectToEquippingPlayers((final Player equippingPlayer) -> !equippingPlayer.getWorld().isDayTime());
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof AbstractSkeleton) || !(BukkitUtil.getDamager(event) instanceof Player playerDamager) || !this.isEquipping(playerDamager)) {
            return;
        }
        event.setDamage(event.getDamage() + (event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) * 0.2));
    }
}
