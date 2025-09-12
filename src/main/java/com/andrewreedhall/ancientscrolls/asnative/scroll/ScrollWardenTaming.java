package com.andrewreedhall.ancientscrolls.asnative.scroll;

import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollWardenTaming extends ItemScrollNative implements Listener {
    private static final String PDK_PET_WARDEN = "scroll_warden_taming_pet_warden";
    private static final int PET_WARDEN_TTL = 600;

    public ScrollWardenTaming() {
        super("warden_taming", "Warden Taming", new String[] {
                "Negates darkness",
                "Summons a player-friendly warden when attacked"
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
        final List<MetadataValue> damagedPlayerPetWardenData = damagedPlayer.getMetadata(PDK_PET_WARDEN);
        Warden damagedPlayerPetWarden;
        if (damagedPlayerPetWardenData.isEmpty()) {
            // summon warden
            damagedPlayerPetWarden = damagingLivingEntity.getWorld().spawn(damagingLivingEntity.getLocation(), Warden.class);
            damagedPlayer.setMetadata(PDK_PET_WARDEN, new FixedMetadataValue(plugin(), damagedPlayerPetWarden));
            damagedPlayerPetWarden.setPersistent(false);
            damagedPlayerPetWarden.setMetadata(PDK_PET_WARDEN, new FixedMetadataValue(plugin(), true));
            plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(
                    plugin(),
                    () -> {
                        damagedPlayer.removeMetadata(PDK_PET_WARDEN, plugin());
                        damagedPlayerPetWarden.remove();
                        final World damagedPlayerPetWardenWorld = damagedPlayerPetWarden.getWorld();
                        damagedPlayerPetWardenWorld.playSound(damagedPlayerPetWarden, Sound.BLOCK_BEACON_DEACTIVATE, 1.0F, 0.75F);
                        damagedPlayerPetWardenWorld.spawnParticle(
                                Particle.SOUL_FIRE_FLAME,
                                damagedPlayerPetWarden.getEyeLocation(),
                                50,
                                1.0,
                                1.0,
                                1.0,
                                0.1
                        );
                    },
                    PET_WARDEN_TTL
            ));
        } else {
            damagedPlayerPetWarden = (Warden) damagedPlayerPetWardenData.getFirst().value();
            if (damagedPlayerPetWarden == null) {
                return;
            }
            damagedPlayerPetWarden.setTarget(damagingLivingEntity);
            damagedPlayerPetWarden.setAnger(damagingLivingEntity, 100);
        }
    }

    @EventHandler
    public void onWardenAngerChange(final WardenAngerChangeEvent event) {
        if (!event.getEntity().hasMetadata(PDK_PET_WARDEN) || !(event.getTarget() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
        event.setNewAnger(0);
    }
}
