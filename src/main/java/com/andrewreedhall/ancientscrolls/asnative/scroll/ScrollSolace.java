package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class ScrollSolace extends ItemScrollNative implements Listener {
    private final NamespacedKey pdckAttackDamageImmunityTTL;

    public ScrollSolace() {
        super("solace", "Solace", new String[] {
                "Invincible to attacks for 2 seconds after attack"
        });
        this.pdckAttackDamageImmunityTTL = this.createSubkey("attack_damage_immunity_ttl");
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final PersistentDataContainer equippingPlayerPDC = equippingPlayer.getPersistentDataContainer();
            final int equippingPlayerAttackDamageImmunityTTL = equippingPlayerPDC.getOrDefault(this.pdckAttackDamageImmunityTTL, PersistentDataType.INTEGER, 0);
            if (equippingPlayerAttackDamageImmunityTTL <= 0) {
                return;
            }
            equippingPlayerPDC.set(this.pdckAttackDamageImmunityTTL, PersistentDataType.INTEGER, equippingPlayerAttackDamageImmunityTTL - 1);
        }, 1L);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !this.isEquipping(damagedPlayer)) {
            return;
        }
        final PersistentDataContainer damagedPlayerPDC = damagedPlayer.getPersistentDataContainer();
        if (damagedPlayerPDC.getOrDefault(this.pdckAttackDamageImmunityTTL, PersistentDataType.INTEGER, 0) <= 0) {
            damagedPlayerPDC.set(this.pdckAttackDamageImmunityTTL, PersistentDataType.INTEGER, 40);
        } else {
            event.setCancelled(true);
        }
    }
}
