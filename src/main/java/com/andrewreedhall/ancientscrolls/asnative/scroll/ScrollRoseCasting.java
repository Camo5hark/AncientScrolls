package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.*;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class ScrollRoseCasting extends ItemScrollNative implements Listener {
    public ScrollRoseCasting() {
        super("rose_casting", "Rose Casting", new String[] {
                "Consume wither roses to launch wither skulls",
                "Slain monsters drop wither roses"
        });
        this.putMCLootTableGenProb("entities/wither", 1.0);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        final Player interactingPlayer = event.getPlayer();
        if (interactingPlayer.getCooldown(Material.WITHER_ROSE) > 0 || !this.isEquipping(interactingPlayer)) {
            return;
        }
        final ItemStack interactingItemStack = event.getItem();
        if (interactingItemStack == null || !interactingItemStack.getType().equals(Material.WITHER_ROSE)) {
            return;
        }
        event.setCancelled(true);
        if (!interactingPlayer.getGameMode().equals(GameMode.CREATIVE)) {
            interactingItemStack.subtract();
        }
        interactingPlayer.setCooldown(interactingItemStack, 20);
        interactingPlayer.launchProjectile(WitherSkull.class, interactingPlayer.getLocation().getDirection());
        final World interactingPlayerWorld = interactingPlayer.getWorld();
        interactingPlayerWorld.playSound(interactingPlayer, Sound.ENTITY_WITHER_SHOOT, 1.0F, 1.0F);
        final Location interactingPlayerEyeLocation = interactingPlayer.getEyeLocation();
        interactingPlayerWorld.spawnParticle(Particle.SMOKE, interactingPlayerEyeLocation, 10, 0.5, 0.5, 0.5, 0.1);
        interactingPlayerWorld.spawnParticle(Particle.SOUL_FIRE_FLAME, interactingPlayerEyeLocation, 10, 0.5, 0.5, 0.5, 0.1);
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (!(event.getDamageSource().getCausingEntity() instanceof Player killingPlayer) || !(event.getEntity() instanceof Monster) || !this.isEquipping(killingPlayer)) {
            return;
        }
        event.getDrops().add(new ItemStack(Material.WITHER_ROSE));
    }
}
