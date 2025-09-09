package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public final class ScrollBallistics extends ItemScrollNative implements Listener {
    public ScrollBallistics() {
        super("ballistics", "Ballistics", new String[] {
                "Doubles projectile velocity"
        });
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("chests/trial_chambers/entrance", 0.31);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        final Projectile launchedProjectile = event.getEntity();
        if (!(launchedProjectile.getShooter() instanceof Player launchingPlayer) || !this.isEquipping(launchingPlayer)) {
            return;
        }
        launchedProjectile.setVelocity(launchedProjectile.getVelocity().multiply(2.0));
        final World launchingPlayerWorld = launchingPlayer.getWorld();
        launchingPlayerWorld.playSound(launchingPlayer, Sound.BLOCK_DISPENSER_LAUNCH, 1.0F, 0.75F);
        launchingPlayerWorld.spawnParticle(Particle.SMOKE, launchingPlayer.getEyeLocation(), 10, 0.5, 0.5, 0.5, 0.1);
    }
}
