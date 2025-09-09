package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollDeflection extends ItemScrollNative implements Listener {
    public ScrollDeflection() {
        super("deflection", "Deflection", new String[] {
                "40% chance arrows will be deflected towards shooter"
        });
        this.putMCLootTableGenProb("chests/jungle_temple", 0.086);
        this.putMCLootTableGenProb("chests/pillager_outpost", 0.11);
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/pillager", 0.01);
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow hittingArrow) ||
                !(hittingArrow.getShooter() instanceof Entity shooter) ||
                !(event.getHitEntity() instanceof Player hitPlayer) ||
                !this.isEquipping(hitPlayer) ||
                plugin().getUniversalRandom().nextDouble() > 0.4
        ) {
            return;
        }
        event.setCancelled(true);
        hittingArrow.remove();
        hitPlayer.launchProjectile(hittingArrow.getClass(), shooter.getLocation().subtract(hitPlayer.getLocation()).toVector().multiply(0.25));
        hitPlayer.getWorld().playSound(hitPlayer, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 1.0F);
    }
}
