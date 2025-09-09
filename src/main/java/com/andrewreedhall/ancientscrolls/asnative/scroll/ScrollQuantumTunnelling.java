package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollQuantumTunnelling extends ItemScrollNative implements Listener {
    public ScrollQuantumTunnelling() {
        super("quantum_tunnelling", "Quantum Tunnelling", new String[] {
                "Teleport to blocks instantly with ender pearls"
        });
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.21);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl launchedEnderPearl) ||
                !(launchedEnderPearl.getShooter() instanceof Player launchingPlayer) ||
                launchingPlayer.getCooldown(Material.ENDER_PEARL) > 0 ||
                !this.isEquipping(launchingPlayer)
        ) {
            return;
        }
        final RayTraceResult rayTraceResult = launchingPlayer.rayTraceBlocks(
                (launchingPlayer.getWorld().getSimulationDistance() - 1) * 16.0
        );
        if (rayTraceResult == null) {
            return;
        }
        final Block hitBlock = rayTraceResult.getHitBlock();
        final BlockFace hitBlockFace = rayTraceResult.getHitBlockFace();
        if (hitBlock == null || hitBlockFace == null) {
            return;
        }
        launchedEnderPearl.remove();
        plugin().scheduleTask(
                (final BukkitScheduler scheduler) ->
                        scheduler.scheduleSyncDelayedTask(
                                plugin(),
                                () -> launchingPlayer.setCooldown(Material.ENDER_PEARL, 60)
                        )
        );
        if (!launchingPlayer.getGameMode().equals(GameMode.CREATIVE)) {
            final PlayerInventory launchingPlayerInventory = launchingPlayer.getInventory();
            ItemStack consumedEnderPearlItemStack = launchingPlayerInventory.getItemInMainHand();
            if (!consumedEnderPearlItemStack.getType().equals(Material.ENDER_PEARL)) {
                consumedEnderPearlItemStack = launchingPlayerInventory.getItemInOffHand();
            }
            consumedEnderPearlItemStack.subtract();
        }
        final Location oldLaunchingPlayerLocation = launchingPlayer.getLocation();
        launchingPlayer.teleport(
                hitBlock
                        .getRelative(hitBlockFace)
                        .getLocation()
                        .add(0.5, 0.0, 0.5)
                        .setDirection(oldLaunchingPlayerLocation.getDirection()),
                PlayerTeleportEvent.TeleportCause.ENDER_PEARL
        );
        launchingPlayer.damage(launchingPlayer.getLocation().distance(oldLaunchingPlayerLocation) * 0.075);
        final World launchingPlayerWorld = launchingPlayer.getWorld();
        launchingPlayerWorld.playSound(launchingPlayer, Sound.ENTITY_PLAYER_TELEPORT, 1.0F, 1.25F);
        launchingPlayerWorld.spawnParticle(
                Particle.DUST,
                launchingPlayer.getEyeLocation(),
                10,
                0.5,
                0.5,
                0.5,
                new Particle.DustOptions(Color.FUCHSIA, 1.0F)
        );
    }
}
