package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollFrostCasting extends ItemScrollNative implements Listener {
    private static final String PMK_FROST_CASTED = "scroll_frost_casting_frost_casted";

    public ScrollFrostCasting() {
        super("frost_casting", "Frost Casting", new String[] {
                "Snowballs:",
                "- do 1 damage",
                "- have a 15% chance of freezing target",
                "- create temporary ice barrier on surfaces"
        });
        this.putMCLootTableGenProb("chests/igloo_chest", 0.25);
        this.putMCLootTableGenProb("chests/village/village_snowy_house", 0.099);
        this.putMCLootTableGenProb("chests/village/village_taiga_house", 0.097);
        this.putMCLootTableGenProb("chests/ancient_city_ice_box", 0.333);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Snowball launchedSnowball) || !(launchedSnowball.getShooter() instanceof Player launchingPlayer) || !this.isEquipping(launchingPlayer)) {
            return;
        }
        launchedSnowball.setMetadata(PMK_FROST_CASTED, new FixedMetadataValue(plugin(), true));
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball hittingSnowball) || !hittingSnowball.hasMetadata(PMK_FROST_CASTED)) {
            return;
        }
        if (event.getHitEntity() instanceof LivingEntity hit) {
            hit.damage(1.0, hittingSnowball);
            final World hitWorld = hit.getWorld();
            final Location hitEyeLocation = hit.getEyeLocation();
            hitWorld.playSound(hit, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0F, 1.0F);
            if (plugin().getUniversalRandom().nextDouble() < 0.15) {
                hit.setFreezeTicks(200);
                hitWorld.playSound(hit, Sound.BLOCK_GLASS_BREAK, 1.0F, 1.5F);
                hitWorld.spawnParticle(Particle.DUST, hitEyeLocation, 10, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.WHITE, 1.0F));
            }
            hitWorld.spawnParticle(Particle.SNOWFLAKE, hitEyeLocation, 10, 0.5, 0.5, 0.5, 0.1);
            return;
        }
        final Block hitBlock = event.getHitBlock();
        final BlockFace hitBlockFace = event.getHitBlockFace();
        if (hitBlock == null || hitBlockFace == null) {
            return;
        }
        Block hitBlockRelative_ = hitBlock.getRelative(hitBlockFace);
        if (!hitBlockRelative_.getType().isAir()) {
            final Block nextHitBlockRelative = hitBlockRelative_.getRelative(hitBlockFace);
            if (!nextHitBlockRelative.getType().isAir()) {
                return;
            }
            hitBlockRelative_ = nextHitBlockRelative;
        }
        final Block hitBlockRelative = hitBlockRelative_;
        hitBlockRelative.setType(Material.PACKED_ICE);
        final World hitBlockRelativeWorld = hitBlockRelative.getWorld();
        final Location hitBlockRelativeLocation = hitBlockRelative.getLocation();
        hitBlockRelativeWorld.playSound(hitBlockRelativeLocation, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0F, 1.0F);
        hitBlockRelativeWorld.spawnParticle(Particle.SNOWFLAKE, hitBlockRelativeLocation, 10, 0.75, 0.75, 0.75, 0.2);
        plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(plugin(), () -> hitBlockRelative.setType(Material.AIR), 60L));
    }
}
