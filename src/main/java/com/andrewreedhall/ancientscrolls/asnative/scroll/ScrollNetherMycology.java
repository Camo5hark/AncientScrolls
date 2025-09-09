package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ScrollNetherMycology extends ItemScrollNative implements Listener {
    public ScrollNetherMycology() {
        super("nether_mycology", "Nether Mycology", new String[] {
                "Eating crimson fungi grants fire resistance",
                "Eating warped fungi grants speed"
        });
        this.putMCLootTableGenProb("chests/bastion_bridge", 0.112);
        this.putMCLootTableGenProb("entities/piglin", 0.005);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final ItemStack interactingItemStack = event.getItem();
        if (interactingItemStack == null) {
            return;
        }
        final Material interactingItemStackType = interactingItemStack.getType();
        switch (interactingItemStackType) {
            case Material.CRIMSON_FUNGUS:
            case Material.WARPED_FUNGUS:
                break;
            default:
                return;
        }
        final Action action = event.getAction();
        if (action.isLeftClick() || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        final Player interactingPlayer = event.getPlayer();
        if (interactingPlayer.getCooldown(interactingItemStack) > 0 || !this.isEquipping(interactingPlayer)) {
            return;
        }
        PotionEffectType potionEffectType = null;
        switch (interactingItemStackType) {
            case Material.CRIMSON_FUNGUS -> {
                potionEffectType = PotionEffectType.FIRE_RESISTANCE;
            }
            case Material.WARPED_FUNGUS -> {
                potionEffectType = PotionEffectType.SPEED;
            }
        }
        if (!interactingPlayer.getGameMode().equals(GameMode.CREATIVE)) {
            interactingItemStack.subtract();
        }
        interactingPlayer.setCooldown(interactingItemStack, 200);
        interactingPlayer.addPotionEffect(new PotionEffect(potionEffectType, 200, 0, false));
        interactingPlayer.getWorld().playSound(interactingPlayer, Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
    }
}
