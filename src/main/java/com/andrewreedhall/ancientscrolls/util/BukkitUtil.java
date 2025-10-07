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
 * Bukkit utility methods
 */
public final class BukkitUtil {
    private static final TooltipDisplay.Builder HIDE_POTION_CONTENTS_TOOLTIP_DISPLAY_BUILDER = TooltipDisplay
            .tooltipDisplay()
            .addHiddenComponents(DataComponentTypes.POTION_CONTENTS);

    /**
     * Plays Sound.BLOCK_NOTE_BLOCK_PLING at half pitch for player<br>
     * For when a player performs an invalid action
     * @param player a player
     */
    public static void playBadSound(final Player player) {
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.5F);
    }

    /**
     *
     * @param event an EntityDamageByEntityEvent
     * @return true if the damaged entity was damaged by a ranged attacker
     */
    public static boolean isRangedDamage(final EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Projectile;
    }

    /**
     * If the damaged entity was damaged by a ranged attacker, the ranged attacker is returned instead of the projectile
     * @param event an EntityDamageByEntityEvent
     * @return the actual damager
     */
    public static Entity getDamager(final EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        return isRangedDamage(event) && ((Projectile) damager).getShooter() instanceof Entity shooter ? shooter : damager;
    }

    /**
     * This method exists because <code>Player#isOnGround()</code> is deprecated
     * @param player a player
     * @return true if the player is on the ground
     */
    public static boolean isPlayerOnGround(final Player player) {
        if (player.isFlying() || player.isGliding() || player.isSwimming() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            return false;
        }
        double yVelocity = player.getVelocity().getY();
        return yVelocity < 0.0 && (player.hasPotionEffect(PotionEffectType.SLOW_FALLING) ? yVelocity == -0.009800000190734863 : yVelocity == -0.0784000015258789);
    }

    /**
     *
     * @param player a player
     * @return true if the player is in a situation where they can perform a critical melee attack (falling and not on ground)
     */
    public static boolean canCrit(final Player player) {
        return player.getFallDistance() > 0.0 && !isPlayerOnGround(player);
    }

    /**
     * Ignores the possible null AttributeInstance returned by <code>LivingEntity#getAttribute(Attribute)</code><br>
     * Just make sure that you know what type of entity you are working with and that it is guaranteed to have the attribute
     * @param livingEntity a living entity
     * @param attribute an attribute
     * @return the entity's attribute instance of attribute
     * @throws NullPointerException if the entity does not have the attribute
     */
    public static AttributeInstance getAttributeInstance(final LivingEntity livingEntity, final Attribute attribute) {
        return Objects.requireNonNull(livingEntity.getAttribute(attribute), "Attribute " + attribute + " is not applicable for living entity " + livingEntity);
    }

    /**
     * AttributeInstance does not have a hasModifier method or something similar
     * @param attributeInstance an attribute instance
     * @param attributeModifierKey a key that points to an attribute modifier in the attribute instance
     * @return true if the attribute instance has an attribute modifier of the key
     */
    public static boolean hasAttributeModifier(final AttributeInstance attributeInstance, final NamespacedKey attributeModifierKey) {
        return attributeInstance.getModifiers().stream().anyMatch((final AttributeModifier attributeModifier) -> attributeModifier.getKey().equals(attributeModifierKey));
    }

    /**
     * Attempts to add an item stack to an inventory<br>
     * If the item stack was not added (inventory is full, restriction, etc.), it is dropped at the inventory holder's location if applicable
     * @param inventory an inventory
     * @param itemStack an item stack
     */
    public static void addItem(final Inventory inventory, final ItemStack itemStack) {
        if (inventory.addItem(itemStack).isEmpty() || !(inventory.getHolder() instanceof Player inventoryHolderPlayer)) {
            return;
        }
        inventoryHolderPlayer.getWorld().dropItem(inventoryHolderPlayer.getEyeLocation(), itemStack);
    }

    /**
     * Ignores the possible null ItemMeta returned by <code>ItemStack#getItemMeta()</code>
     * @param itemStack an item stack
     * @return a snapshot of the item stack's item meta
     * @throws NullPointerException if the item stack's item meta is null (is this even possible?)
     */
    public static ItemMeta getItemMeta(final ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.getItemMeta(), "ItemMeta is null for ItemStack " + itemStack);
    }

    public static void hidePotionContentsTooltipDisplay(final ItemStack itemStack) {
        itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, HIDE_POTION_CONTENTS_TOOLTIP_DISPLAY_BUILDER);
    }
}
