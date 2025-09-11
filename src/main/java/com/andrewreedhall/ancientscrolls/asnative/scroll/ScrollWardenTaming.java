package com.andrewreedhall.ancientscrolls.asnative.scroll;

import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ScrollWardenTaming extends ItemScrollNative implements Listener {
    public ScrollWardenTaming() {
        super("warden_taming", "Warden Taming", new String[] {
                "Negates darkness",
                "Wardens will not target user",
                "Summons a warden when attacked"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/warden", 1.0);
    }

    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {
        this.negatePotionEffectType(event, PotionEffectType.DARKNESS);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) ||
                !(event.getDamager() instanceof LivingEntity damagingLivingEntity) ||
                !this.isEquipping(damagedPlayer)
        ) {
            return;
        }
        final Warden damagedPlayerPetWarden = damagingLivingEntity.getWorld().spawn(damagingLivingEntity.getLocation(), Warden.class);
        damagedPlayerPetWarden.setPersistent(false);
    }

    @EventHandler
    public void onWardenAngerChange(final WardenAngerChangeEvent event) {
        if (!(event.getTarget() instanceof Player targettedPlayer) || !this.isEquipping(targettedPlayer)) {
            return;
        }
        event.setCancelled(true);
        event.setNewAnger(0);
    }
}
