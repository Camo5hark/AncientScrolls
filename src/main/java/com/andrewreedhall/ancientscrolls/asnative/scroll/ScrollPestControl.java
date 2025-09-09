package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public final class ScrollPestControl extends ItemScrollNative implements Listener {
    private static final Set<EntityType> ENTITY_TYPES = Set.of(
            EntityType.SPIDER,
            EntityType.SILVERFISH,
            EntityType.ENDERMITE
    );

    public ScrollPestControl() {
        super("pest_control", "Pest Control", new String[] {
                "Negates:",
                "- infested effect",
                "- damage from regular spiders, silverfish, and endermites"
        });
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.INFESTED);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !ENTITY_TYPES.contains(event.getDamager().getType()) || !this.isEquipping(damagedPlayer)) {
            return;
        }
        event.setCancelled(true);
    }
}
