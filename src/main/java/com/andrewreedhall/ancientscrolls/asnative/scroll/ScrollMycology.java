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

public final class ScrollMycology extends ItemScrollNative implements Listener {
    public ScrollMycology() {
        super("mycology", "Mycology", new String[] {
                "Eating brown mushrooms grants regeneration",
                "Eating red mushrooms grants strength"
        });
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.255);
        this.putMCLootTableGenProb("entities/witch", 0.005);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final ItemStack interactingItemStack = event.getItem();
        if (interactingItemStack == null) {
            return;
        }
        final Material interactingItemStackType = interactingItemStack.getType();
        switch (interactingItemStackType) {
            case Material.BROWN_MUSHROOM:
            case Material.RED_MUSHROOM:
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
            case Material.BROWN_MUSHROOM -> {
                potionEffectType = PotionEffectType.STRENGTH;
            }
            case Material.RED_MUSHROOM -> {
                potionEffectType = PotionEffectType.REGENERATION;
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
