/*
AncientScrolls SpigotMC plugin
Copyright (C) 2025  Andrew Hall

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls.util;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

/**
 * Utility methods for Bukkit-related operations.
 */
public final class BukkitUtil {
    private static final TooltipDisplay.Builder HIDE_POTION_CONTENTS_TOOLTIP_DISPLAY_BUILDER = TooltipDisplay
            .tooltipDisplay()
            .addHiddenComponents(DataComponentTypes.POTION_CONTENTS);

    /**
     * Plays a negative feedback sound to a player.
     * @param player the player
     */
    public static void playBadSound(final Player player) {
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.5F);
    }

    /**
     * Checks if the damage event was caused by a ranged attack.
     * @param event the damage event
     * @return true if ranged damage
     */
    public static boolean isRangedDamage(final EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Projectile;
    }

    /**
     * Gets the true damager entity, accounting for projectiles.
     * @param event the damage event
     * @return the damager entity
     */
    public static Entity getDamager(final EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        return isRangedDamage(event) && ((Projectile) damager).getShooter() instanceof Entity shooter ? shooter : damager;
    }

    /**
     * Checks if the player is on the ground.
     * @param player the player
     * @return true if on ground
     */
    public static boolean isPlayerOnGround(final Player player) {
        if (player.isFlying() || player.isGliding() || player.isSwimming() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            return false;
        }
        double yVelocity = player.getVelocity().getY();
        return yVelocity < 0.0 && (player.hasPotionEffect(PotionEffectType.SLOW_FALLING) ? yVelocity == -0.009800000190734863 : yVelocity == -0.0784000015258789);
    }

    /**
     * Checks if a player can perform a critical hit.
     * @param player the player
     * @return true if critical hit is possible
     */
    public static boolean canCrit(final Player player) {
        return player.getFallDistance() > 0.0 && !isPlayerOnGround(player);
    }

    /**
     * Gets an attribute instance from a living entity.
     * @param livingEntity the entity
     * @param attribute the attribute
     * @return the attribute instance
     * @throws NullPointerException if not applicable
     */
    public static AttributeInstance getAttributeInstance(final LivingEntity livingEntity, final Attribute attribute) {
        return Objects.requireNonNull(livingEntity.getAttribute(attribute), "Attribute " + attribute + " is not applicable for living entity " + livingEntity);
    }

    /**
     * Checks if an attribute instance has a modifier with the given key.
     * @param attributeInstance the attribute instance
     * @param attributeModifierKey the modifier key
     * @return true if found
     */
    public static boolean hasAttributeModifier(final AttributeInstance attributeInstance, final NamespacedKey attributeModifierKey) {
        return attributeInstance.getModifiers().stream().anyMatch((final AttributeModifier attributeModifier) -> attributeModifier.getKey().equals(attributeModifierKey));
    }

    /**
     * Adds an item to an inventory or drops it if full and the holder is a player.
     * @param inventory the inventory
     * @param itemStack the item to add
     */
    public static void addItem(final Inventory inventory, final ItemStack itemStack) {
        if (inventory.addItem(itemStack).isEmpty() || !(inventory.getHolder() instanceof Player inventoryHolderPlayer)) {
            return;
        }
        inventoryHolderPlayer.getWorld().dropItem(inventoryHolderPlayer.getEyeLocation(), itemStack);
    }

    /**
     * Gets non-null item meta from an item stack.
     * @param itemStack the item stack
     * @return the item meta
     * @throws NullPointerException if meta is null
     */
    public static ItemMeta getItemMeta(final ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.getItemMeta(), "ItemMeta is null for ItemStack " + itemStack);
    }

    /**
     * Hides potion contents from an item's tooltip.
     * @param itemStack the item stack
     */
    public static void hidePotionContentsTooltipDisplay(final ItemStack itemStack) {
        itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, HIDE_POTION_CONTENTS_TOOLTIP_DISPLAY_BUILDER);
    }
}
