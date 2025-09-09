package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class ScrollSniping extends ItemScrollNative implements Listener {
    public ScrollSniping() {
        super("sniping", "Sniping", new String[] {
                "Arrow hitscan"
        });
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.21);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow launchedArrow) || !(launchedArrow.getShooter() instanceof Player launchingPlayer) || !this.isEquipping(launchingPlayer)) {
            return;
        }
        final World launchingPlayerWorld = launchingPlayer.getWorld();
        final Vector launchingPlayerDirection = launchingPlayer.getLocation().getDirection();
        final RayTraceResult rayTraceResult = launchingPlayerWorld.rayTraceEntities(
                launchingPlayer.getEyeLocation(),
                launchingPlayerDirection,
                (launchingPlayerWorld.getSimulationDistance() - 1) * 16.0,
                1.0,
                (final Entity rayTracedEntity) ->
                        rayTracedEntity instanceof LivingEntity && launchedArrow.canHitEntity(rayTracedEntity)
        );
        if (rayTraceResult == null) {
            return;
        }
        final LivingEntity hitLivingEntity = (LivingEntity) rayTraceResult.getHitEntity();
        if (hitLivingEntity == null) {
            return;
        }
        launchedArrow.hitEntity(hitLivingEntity, launchingPlayerDirection);
        launchingPlayerWorld.playSound(launchingPlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        hitLivingEntity.getWorld().spawnParticle(Particle.DUST, hitLivingEntity.getEyeLocation(), 50, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.MAROON, 1.5F));
    }
}
