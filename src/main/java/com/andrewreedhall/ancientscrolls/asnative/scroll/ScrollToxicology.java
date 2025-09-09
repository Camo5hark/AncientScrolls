package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollToxicology extends ItemScrollNative implements Listener {
    public ScrollToxicology() {
        super("toxicology", "Toxicology", new String[] {
                "Negates poison damage",
                "Poison melee targets when poisoned"
        });
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) ||
                !event.getCause().equals(EntityDamageEvent.DamageCause.POISON) ||
                !this.isEquipping(damagedPlayer)
        ) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity damagedLivingEntity) ||
                !(event.getDamager() instanceof Player damagingPlayer) ||
                !damagingPlayer.hasPotionEffect(PotionEffectType.POISON) ||
                !this.isEquipping(damagingPlayer)
        ) {
            return;
        }
        plugin().getMonsterPoisonSimulator().add(damagedLivingEntity, 80);
    }
}
