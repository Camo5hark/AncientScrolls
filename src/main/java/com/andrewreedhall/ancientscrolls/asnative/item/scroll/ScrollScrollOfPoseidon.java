package com.andrewreedhall.ancientscrolls.asnative.item.scroll;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollScrollOfPoseidon extends ItemScrollNative implements Listener {
    private static final int RADIUS = 5;
    private static final String PDK_RAIN_TRIDENT = "scroll_scroll_of_poseidon_rain_trident";

    public ScrollScrollOfPoseidon() {
        super("scroll_of_poseidon", "Scroll of Poseidon", new String[] {
                "Launching trident causes raining tridents",
                "Raining tridents poison target"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/elder_guardian", 1.0);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Trident launchedTrident) ||
                !(launchedTrident.getShooter() instanceof Player launchingPlayer) ||
                !this.isEquipping(launchingPlayer)
        ) {
            return;
        }
        final World launchingPlayerWorld = launchingPlayer.getWorld();
        final Location launchingPlayerLocation = launchingPlayer.getLocation();
        for (int i = 0; i < 10; ++i) {
            final Trident trident = launchingPlayerWorld.spawn(
                    launchingPlayerLocation.clone().add(
                            getTridentRelXOrZ(),
                            RADIUS,
                            getTridentRelXOrZ()
                    ),
                    Trident.class
            );
            trident.setMetadata(PDK_RAIN_TRIDENT, new FixedMetadataValue(plugin(), true));
            trident.setPersistent(false);
            trident.setShooter(launchingPlayer);
            trident.getLocation().setDirection(new Vector(0.0, -1.0, 0.0));
            trident.getVelocity().multiply(6.0);
            launchingPlayerWorld.spawnParticle(Particle.CLOUD, trident.getLocation(), 10, 0.25, 0.25, 0.25, 0.01);
        }
        launchingPlayerWorld.playSound(launchingPlayer, Sound.ENTITY_GUARDIAN_HURT, 1.0F, 1.0F);
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident hittingTrident) || !hittingTrident.hasMetadata(PDK_RAIN_TRIDENT)) {
            return;
        }
        if (event.getHitBlock() != null) {
            hittingTrident.remove();
        } else if (event.getHitEntity() instanceof LivingEntity hitLivingEntity) {
            plugin().getMonsterPoisonSimulator().add(hitLivingEntity, 60);
        }
        hittingTrident.getWorld().spawnParticle(Particle.BUBBLE, hittingTrident.getLocation(), 25, 0.5, 1.0, 0.5, 0.1);
    }

    private static double getTridentRelXOrZ() {
        return (plugin().getUniversalRandom().nextBoolean() ? 1.0 : -1.0) * plugin().getUniversalRandom().nextDouble(RADIUS * 0.2, RADIUS);
    }
}
