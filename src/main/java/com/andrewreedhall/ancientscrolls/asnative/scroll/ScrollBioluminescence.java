package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollBioluminescence extends ItemScrollNative {
    public ScrollBioluminescence() {
        super("bioluminescence", "Bioluminescence", new String[] {
                "Emit light"
        });
        // TODO dungeon
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.282);
        this.putMCLootTableGenProb("chests/ancient_city", 0.161);
        this.putMCLootTableGenProb("entities/blaze", 0.005);
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            if (!equippingPlayer.getEyeLocation().getBlock().getType().isAir()) {
                return;
            }
            final Collection<Player> nearbyPlayers = getNearbyPlayers(equippingPlayer);
            final Location equippingPlayerEyeBlockLocation = equippingPlayer.getEyeLocation().getBlock().getLocation();
            final Light lightBlockData = (Light) Material.LIGHT.createBlockData();
            lightBlockData.setLevel(15);
            nearbyPlayers.forEach((final Player nearbyPlayer) -> nearbyPlayer.sendBlockChange(equippingPlayerEyeBlockLocation, lightBlockData));
            final BlockData airBlockData = Material.AIR.createBlockData();
            plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(
                    plugin(),
                    () -> nearbyPlayers.forEach((final Player nearbyPlayer) -> {
                        if (equippingPlayer.getEyeLocation().getBlock().getLocation().equals(equippingPlayerEyeBlockLocation)) {
                            return;
                        }
                        nearbyPlayer.sendBlockChange(equippingPlayerEyeBlockLocation, airBlockData);
                    }),
                    4L
            ));
        }, 4L);
    }

    private static @NotNull Collection<Player> getNearbyPlayers(Player equippingPlayer) {
        final World eqippingPlayerWorld = equippingPlayer.getWorld();
        final Location equippingPlayerLocation = equippingPlayer.getLocation();
        final double radius = (eqippingPlayerWorld.getSimulationDistance() - 1) << 4;
        return eqippingPlayerWorld.getNearbyPlayers(
                equippingPlayerLocation,
                radius,
                (final Player nearbyPlayer) -> nearbyPlayer.getLocation().distance(equippingPlayerLocation) <= radius
        );
    }
}
