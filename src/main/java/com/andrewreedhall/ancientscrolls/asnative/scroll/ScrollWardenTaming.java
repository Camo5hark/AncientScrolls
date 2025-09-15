package com.andrewreedhall.ancientscrolls.asnative.scroll;

import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.Objects;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollWardenTaming extends ItemScrollNative implements Listener {
    private static final String PMK_PET_WARDEN = "scroll_warden_taming_pet_warden";
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
        final List<MetadataValue> damagedPlayerPetWardenData = damagedPlayer.getMetadata(PMK_PET_WARDEN);
        Warden damagedPlayerPetWarden;
        if (damagedPlayerPetWardenData.isEmpty()) {
            // summon warden
            damagedPlayerPetWarden = damagingLivingEntity.getWorld().spawn(damagingLivingEntity.getLocation(), Warden.class);
            damagedPlayer.setMetadata(PMK_PET_WARDEN, new FixedMetadataValue(plugin(), damagedPlayerPetWarden));
            damagedPlayerPetWarden.setMetadata(PMK_PET_WARDEN, new FixedMetadataValue(plugin(), damagedPlayer));
            damagedPlayerPetWarden.setPersistent(false);
            final World damagedPlayerPetWardenWorld = damagedPlayerPetWarden.getWorld();
            damagedPlayerPetWardenWorld.playSound(damagedPlayerPetWarden, Sound.ENTITY_WARDEN_EMERGE, 1.0F, 0.75F);
            damagedPlayerPetWardenWorld.spawnParticle(
                    Particle.DUST,
                    damagedPlayerPetWarden.getEyeLocation(),
                    50,
                    1.0,
                    1.0,
                    1.0,
                    new Particle.DustOptions(Color.NAVY, 1.25F)
            );
            plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(
                    plugin(),
                    () -> {
                        damagedPlayer.removeMetadata(PMK_PET_WARDEN, plugin());
                        if (damagedPlayerPetWarden.isDead()) {
                            return;
                        }
                        damagedPlayerPetWarden.remove();
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
        if (!event.getEntity().hasMetadata(PMK_PET_WARDEN) || !(event.getTarget() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
        event.setNewAnger(0);
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Warden deadWarden)) {
            return;
        }
        final List<MetadataValue> deadWardenPetWardenOwner = deadWarden.getMetadata(PMK_PET_WARDEN);
        if (deadWardenPetWardenOwner.isEmpty()) {
            return;
        }
        ((Player) Objects.requireNonNull(deadWardenPetWardenOwner.getFirst().value())).removeMetadata(PMK_PET_WARDEN, plugin());
    }
}
