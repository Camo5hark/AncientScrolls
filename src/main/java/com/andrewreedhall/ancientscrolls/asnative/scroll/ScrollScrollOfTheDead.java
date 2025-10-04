package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public final class ScrollScrollOfTheDead extends ItemScrollNative implements Listener {
    public ScrollScrollOfTheDead() {
        super("scroll_of_the_dead", "Scroll of the Dead", new String[] {
                "Negates undead monster targeting",
                "Nearby undead monsters target whatever user attacks"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/wither", 1.0);
    }

    @EventHandler
    public void onEntityTargetLivingEntity(final EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player targetedPlayer) ||
                !CommonSets.UNDEAD_MONSTERS.contains(event.getEntity().getType()) ||
                !this.isEquipping(targetedPlayer)
        ) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity damagedLivingEntity) ||
                !(BukkitUtil.getDamager(event) instanceof Player damagingPlayer) ||
                !this.isEquipping(damagingPlayer)
        ) {
            return;
        }
        final World damagingPlayerWorld = damagingPlayer.getWorld();
        damagingPlayerWorld.getNearbyLivingEntities(
                damagingPlayer.getLocation(),
                Math.min((damagingPlayerWorld.getSimulationDistance() - 1) << 4, 25.0),
                (final LivingEntity nearbyLivingEntity) -> nearbyLivingEntity instanceof Monster && CommonSets.UNDEAD_MONSTERS.contains(nearbyLivingEntity.getType())
        ).forEach((final LivingEntity nearbyLivingEntity) -> {
            final Monster nearbyUndeadMonster = (Monster) nearbyLivingEntity;
            nearbyUndeadMonster.setTarget(damagedLivingEntity);
            spawnParticle(nearbyUndeadMonster);
        });
        spawnParticle(damagedLivingEntity);
    }

    private static void spawnParticle(final LivingEntity livingEntity) {
        livingEntity.getWorld().spawnParticle(
                Particle.TRIAL_OMEN,
                livingEntity.getEyeLocation(),
                10,
                0.5,
                0.5,
                0.5,
                0.1
        );
    }
}
